package com.kep.iruma.common.service

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.kep.iruma.common.exception.IrumaRuntimeException
import com.kep.iruma.common.exception.ResultCode
import com.kep.iruma.common.util.JsonUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.slf4j.Logger

interface ApiCallService {
    fun get(url: String,
        headers: Map<String, String> = emptyMap(),
        parameters: List<Pair<String, Any?>> = emptyList()
    ): Response

    fun post(url: String,
        headers: Map<String, String> = emptyMap(),
        parameters: List<Pair<String, Any?>> = emptyList(),
        body: Any?): Response
}

@Component
class ApiCallServiceImpl : ApiCallService {
    private val fileLogger: Logger = LoggerFactory.getLogger("STACKTRACE_LOGGER")

    override fun get(url: String, headers: Map<String, String>, parameters: List<Pair<String, Any?>>): Response {
        return Fuel.get(url, parameters)
            .header(headers)
            .response()
            .getResponse()
    }

    override fun post(url: String, headers: Map<String, String>, parameters: List<Pair<String, Any?>>, body: Any?): Response {
        return Fuel.post(url, parameters)
            .header(headers)
            .serializeBody(body)
            .response()
            .getResponse()
    }

    private fun Request.serializeBody(bodyObject: Any?): Request {
        val bodyString: String = bodyObject?.let {
            JsonUtil.toJsonString(it)
        } ?: return this

        return body(bodyString, Charsets.UTF_8)
    }

    private fun ResponseResultOf<ByteArray>.isSuccess(): Boolean {
        return this.second.isSuccessful
    }

    private fun ResponseResultOf<ByteArray>.getResponse(): Response {
        val response = this.second

        // TODO: 예외처리 방식 개선
        if (response.statusCode == -1) {
            val exception = this.third.component2()?.exception
            exception?.let {
                fileLogger.error("{}", it)
            }
            throw IrumaRuntimeException(ResultCode.API_CALL_ERROR)
        }

        return response
    }
}
