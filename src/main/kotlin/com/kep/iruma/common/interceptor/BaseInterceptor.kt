package com.kep.iruma.common.interceptor

import com.kep.iruma.common.constant.HeaderConstant
import com.kep.iruma.common.util.HeaderUtil
import com.kep.iruma.common.util.ThreadContextUtil
import org.springframework.lang.Nullable
import org.springframework.web.servlet.AsyncHandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BaseInterceptor : AsyncHandlerInterceptor {
    // Header 정보를 HeaderUtil 에서 뽑아 ThreadContextUtil 내부의 ThreadLocal에 세팅.
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // x-request-id 우선순위: | 1.requestHeader의 X-Request-ID 값 | 2.없으면 생성 (IRUMA-xxxxxxxxxxx)
        HeaderUtil.extractXRequestId(request)?.let { ThreadContextUtil.setXRequestId(it) }

        //todo : token 인증 관련 처리 (with KEP-IAM)
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, @Nullable modelAndView: ModelAndView?) {
        response.setHeader(HeaderConstant.X_REQUEST_ID_KEY, ThreadContextUtil.getXRequestId())
    }
}
