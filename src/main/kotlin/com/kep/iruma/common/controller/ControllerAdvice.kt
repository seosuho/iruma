package com.kep.iruma.common.controller

import com.kep.iruma.common.exception.IrumaRuntimeException
import com.kep.iruma.common.exception.ResultCode
import com.kep.iruma.common.response.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler
    fun handleExpectedException(ex: IrumaRuntimeException): ResponseEntity<RestResponse> {
        //todo
//        if (ex.resultCode.shouldReport) {
//
//        }
        //todo
//        if (ex.resultCode.shouldAlert) {
//
//        }

        return ResponseEntity(RestResponse(ex.resultCode), ex.resultCode.httpStatus)
    }

    @ExceptionHandler
    protected fun handleUnexpectedException(exception: Exception): ResponseEntity<RestResponse> {
        return ResponseEntity(RestResponse(ResultCode.UNEXPECTED_ERROR), ResultCode.UNEXPECTED_ERROR.httpStatus)
    }
}
