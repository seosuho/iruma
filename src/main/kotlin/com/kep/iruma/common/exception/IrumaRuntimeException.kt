package com.kep.iruma.common.exception

import java.lang.RuntimeException

class IrumaRuntimeException (
    val resultCode: ResultCode
) : RuntimeException(resultCode.defaultMessage)
