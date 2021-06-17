package com.kep.iruma.common.util

import java.util.Arrays
import java.util.stream.Collectors

fun extractAppStackTrace(exception: Exception?): List<StackTraceElement>? {
    return exception?.let {
        Arrays.stream(exception.stackTrace)
            .filter { stackTraceElement: StackTraceElement -> stackTraceElement.className.startsWith("com.kep.iruma") }
            .collect(Collectors.toList())
    } ?: emptyList()
}

//todo : matrix 설정 끝나면 적용
//fun reportError(exception: Exception) {
//    Sentry.getContext().clearTags()
//    Sentry.getContext().addTag("shouldAlert", if(exception is KittyRuntimeException) exception.resultCode.shouldAlert.toString() else "true")
//    Sentry.getContext().addTag("phase", KittyConfigHolder.AppProfile.phase)
//    Sentry.getContext().addTag("x-request-id", ThreadContextUtil.getXRequestId())
//    Sentry.capture(exception)
//}
