package com.kep.iruma.common.exception

import org.springframework.http.HttpStatus

// 응답 종류에 따른 응답코드, HttpStatus, 응답메시지 등을 정의함.
enum class ResultCode(
    val httpStatus: HttpStatus,
    val code: Int,
    val defaultMessage: String,
    val shouldReport: Boolean = false,  // 보고 여부 (Matrix)
    val shouldAlert: Boolean = false
) { // 알람 여부 (WatchTower)

    // todo: shouldReport = false 인 경우 Matrix에 오류가 보고 되지 않도록 처리
    // todo: shouldAlert = false인 경우 Matrix의 Watchtower 알림이 작동하지 않도록 처리

    OK(HttpStatus.OK, 200_00_000, "OK"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400_00_000, "This is invalid request."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401_00_000, "Unauthorized", true, false),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404_00_000, "Not found"),

    API_CALL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500_01_000, "Failed to call an external API", true, true),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500_99_999, "Unexpected error", true, true),
    ;

    fun get(code: Int): ResultCode {
        return values().firstOrNull { it.code == code } ?: UNEXPECTED_ERROR
    }
}
