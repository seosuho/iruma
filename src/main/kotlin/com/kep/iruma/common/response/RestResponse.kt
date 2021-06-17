package com.kep.iruma.common.response

import com.kep.iruma.common.exception.ResultCode

class RestResponse(
    val code: Int,
    val message: String,
    val result: Any?
) {
    constructor(resultCode: ResultCode, result: Any?) : this(resultCode.code, resultCode.defaultMessage, result)

    constructor(resultCode: ResultCode) : this(code = resultCode.code, message = resultCode.defaultMessage)

    constructor(code: Int, message: String) : this(code, message, null)
}
