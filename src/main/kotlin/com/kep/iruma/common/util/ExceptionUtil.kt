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