package com.kep.iruma.common.config

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.kep.iruma.common.constant.HeaderConstant
import com.kep.iruma.common.util.JsonUtil
import com.kep.iruma.common.util.ThreadContextUtil
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.annotation.PostConstruct

@Configuration
class FuelConfig {
    companion object {
        val httpClientLogger = LoggerFactory.getLogger("HTTP_CLIENT_LOGGER") // logger를 static에 우겨넣기
    }

    // default header, timeout 세팅 및 로깅관련 설정.
    @PostConstruct
    private fun addFuelInterceptors(): FuelManager {
        return FuelManager.instance.apply {
            addRequestInterceptor { next: (Request) -> Request ->
                { request: Request ->
                    request.header(HeaderConstant.CONTENT_TYPE_KEY, MediaType.APPLICATION_JSON_VALUE)
                        .header(HeaderConstant.X_REQUEST_ID_KEY, ThreadContextUtil.getXRequestId())
                        .timeout(15000) //FIXME :: timeout의 경우 임의로 설정함 이후 property화 할지 고정값으로 가져갈지 정해서 수정하기
                        .timeoutRead(15000)
                    request.tag(Instant.now())
                    next(request)
                }
            }
            // todo : request, response 전체 로깅
            addResponseInterceptor { next: (Request, Response) -> Response ->
                { request: Request, response: Response ->
                    val startTime = request.getTag(Instant::class)
                    val endTime = Instant.now()
                    val requestLoggingMap = hashMapOf(
                        "url" to request.url.toString(),
                        "host" to request.url.host,
                        "method" to request.method.value,
                        "header" to request.headers.keys.map { it to request.headers[it].joinToString(",") }.toMap()
                            .plus("kw-user" to ThreadContextUtil.getUserId().toString()),
                        "query" to request.url.query,
                        "body" to JsonUtil.toObject(request.body.toByteArray())
                    )
                    val responseLoggingMap = hashMapOf(
                        "status" to response.statusCode,
                        "header" to response.headers.keys.map { it to response.headers[it].joinToString(",") }.toMap()
                            .plus("kw-user" to ThreadContextUtil.getUserId().toString()),
                        "body" to JsonUtil.toObject(response.body().toByteArray())
                    )

                    httpClientLogger.info("", StructuredArguments.value("loggedAt", OffsetDateTime.ofInstant(endTime, ZoneId.of("Asia/Seoul")).toString()) // kibana에서 timestamp 설정 시 이 값으로 해주세요.
                        , StructuredArguments.value("elapsedTime", Duration.between(startTime, endTime).toMillis())
//                            , StructuredArguments.value("phase", KittyConfigHolder.AppProfile.phase)
                        , StructuredArguments.value("logType", "httpClient"), StructuredArguments.value("request", requestLoggingMap), StructuredArguments.value("response", responseLoggingMap)
                    )

                    next(request, response)
                }
            }
        }
    }
}
