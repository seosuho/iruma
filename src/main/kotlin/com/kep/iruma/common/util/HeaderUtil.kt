package com.kep.iruma.common.util

import com.kep.iruma.common.constant.HeaderConstant
import javax.servlet.http.HttpServletRequest

// inbound & outbound Header 관련 Util 입니다.
object HeaderUtil {
    fun extractXRequestId(request: HttpServletRequest): String? {
        return request.getHeader(HeaderConstant.X_REQUEST_ID_KEY)
    }

    fun getAuthToken(request: HttpServletRequest): String? {
        return request.getHeader(HeaderConstant.AUTHORIZATION_KEY)
    }
}
