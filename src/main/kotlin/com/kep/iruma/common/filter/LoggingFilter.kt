package com.kep.iruma.common.filter

import com.kep.iruma.common.constant.HeaderConstant
import com.kep.iruma.common.util.JsonUtil
import com.kep.iruma.common.util.ThreadContextUtil
import com.kep.iruma.common.util.extractAppStackTrace
import net.logstash.logback.argument.StructuredArguments

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.context.support.SpringBeanAutowiringSupport
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.time.Duration
import java.time.Instant
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingFilter() : OncePerRequestFilter() {

    private val accessLogger: Logger = LoggerFactory.getLogger("ACCESS_LOGGER")

    override fun initFilterBean() {
        super.initFilterBean()
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, this.servletContext)
    }

    override fun doFilterInternal(originalRequest: HttpServletRequest, originalResponse: HttpServletResponse, filterChain: FilterChain) {
        val startTime = Instant.now()
        val request = ContentCachingRequestWrapper(originalRequest)
        val response = ContentCachingResponseWrapper(originalResponse)

        var requestLoggingMap: Map<String, Any?>? = null
        var responseLoggingMap: Map<String, Any?>? = null

        try {
            filterChain.doFilter(request, response)
            requestLoggingMap = makeRequestMapForLogging(request)
            responseLoggingMap = makeResponseMapForLogging(response)
            val elapsedTime = Duration.between(startTime, Instant.now()).toMillis()
            accessLogger.info(""
                , StructuredArguments.value("elapsedTime", elapsedTime)
//                    , StructuredArguments.value("phase", KittyConfigHolder.AppProfile.phase)
                , StructuredArguments.value("logType", "access")
                , StructuredArguments.value("x-request-id", ThreadContextUtil.getXRequestId() )
                , StructuredArguments.value("request", requestLoggingMap)
                , StructuredArguments.value("response", responseLoggingMap)
                , StructuredArguments.value("exception", extractAppStackTrace(ThreadContextUtil.getException()))
            )
        } catch (e: Exception) {
            // Catch Exception caused by processing requestBody (because of logging)
            accessLogger.error(""
//                    , StructuredArguments.value("phase", KittyConfigHolder.AppProfile.phase)
                , StructuredArguments.value("logType", "access")
                , StructuredArguments.value("request", requestLoggingMap)
                , StructuredArguments.value("response", responseLoggingMap)
                , StructuredArguments.value("exception", extractAppStackTrace(e)))
        } finally {
            response.copyBodyToResponse()
            response.characterEncoding = "UTF-8"
            ThreadContextUtil.removeAllThreadContexts()
        }
    }

    private fun makeRequestMapForLogging(request: ContentCachingRequestWrapper): Map<String, Any?> {
        // expected result: { "headerKey" : "headerValue1", "headerKey2": "headerValue2, headerValue3" }
        val requestHeaderMap = mutableMapOf<String, String>()
        request.headerNames.toList().associateWithTo(requestHeaderMap, { request.getHeaders(it).toList().joinToString(",") })
        if(request.getHeader(HeaderConstant.X_REQUEST_ID_KEY) == null) {
            requestHeaderMap.putIfAbsent(HeaderConstant.X_REQUEST_ID_KEY, ThreadContextUtil.getXRequestId()) // x-request-id가 없을 경우 내부적으로 만든 값을 추가.
        }

        // expected result: { "paramName1": "paramValue1", "paramName2": "paramValue2, paramValue3" }
        val requestParameterMap = request.parameterNames.toList().map {
            it to request.getParameterValues(it).joinToString(",")
        }.toMap()

        // request body (application/json)
        val requestBodyObject = JsonUtil.toObject(request.contentAsByteArray)

        // request info
        return hashMapOf(
            "url" to request.requestURL.toString(),
            "queryString" to request.queryString,
            "method" to request.method,
            "remoteAddr" to request.remoteAddr,
            "remoteHost" to request.remoteHost,
            "remotePort" to request.remotePort,
            "remoteUser" to request.remoteUser,
            "encoding" to request.characterEncoding,
            "header" to requestHeaderMap,
            "param" to requestParameterMap,
            "body" to requestBodyObject
        )
    }

    private fun makeResponseMapForLogging(response: ContentCachingResponseWrapper): Map<String, Any?> {

        // response header
        val responseHeaderMap = response.headerNames.toList().map{
            it to response.getHeaders(it).toList().joinToString ( "," )
        }.toMap()

        // response body
        val requestBodyObj = JsonUtil.toObject(response.contentAsByteArray)

        // response info
        return hashMapOf(
            "status" to response.status,
            "header" to responseHeaderMap,
            "body" to requestBodyObj
        )
    }
}
