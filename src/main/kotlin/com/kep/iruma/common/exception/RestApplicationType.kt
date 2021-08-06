package com.kep.iruma.common.exception

import com.kep.iruma.common.constant.ApiErrorCode
import org.springframework.http.HttpStatus

data class RestApplicationType(
    val httpStatus: HttpStatus,
    val code: Int,
    private val _message: String? = null,
    private val _cause: Throwable? = null
) : RuntimeException(_message, _cause) {

    // Bad Request(400).
    fun requiredFieldMissing(fieldName: String) = RestApplicationType(HttpStatus.BAD_REQUEST, ApiErrorCode.REQUIRED_FIELD_MISSING, "$fieldName is required.")
    fun invalidFieldFormat(fieldName: String, message: String) = RestApplicationType(HttpStatus.BAD_REQUEST, ApiErrorCode.INVALID_FIELD_FORMAT, message)
    fun invalidJsonFormat() = RestApplicationType(HttpStatus.BAD_REQUEST, ApiErrorCode.INVALID_JSON_FORMAT, "invalid json format.")

    // Unauthorized(401).
    fun unauthorized() = RestApplicationType(HttpStatus.UNAUTHORIZED, ApiErrorCode.UNAUTHORIZED, "unauthorized.")

    // Forbidden(403).
    fun forbidden() = RestApplicationType(HttpStatus.FORBIDDEN, ApiErrorCode.FORBIDDEN, "access denied.")

    // NotFound(404).
    fun notFound() = RestApplicationType(HttpStatus.NOT_FOUND, ApiErrorCode.NOT_FOUND, "not found")

    // Internal Server Error(500).
    fun unknownError() = RestApplicationType(HttpStatus.INTERNAL_SERVER_ERROR, ApiErrorCode.UNKNOWN_ERROR, "unknown error")

}
