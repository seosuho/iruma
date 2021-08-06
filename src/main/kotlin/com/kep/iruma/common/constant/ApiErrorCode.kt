package com.kep.iruma.common.constant

object ApiErrorCode {
    // 400
    const val REQUIRED_FIELD_MISSING = 102
    const val INVALID_FIELD_FORMAT = 103
    const val INVALID_JSON_FORMAT = 104

    // 401
    const val UNAUTHORIZED = 1001

    // 403
    const val FORBIDDEN = 1199


    // 404
    const val NOT_FOUND = 1299

    // 500
    const val UNKNOWN_ERROR = 10001
}
