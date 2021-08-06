package com.kep.iruma.common.util

import java.util.*

// thread 별로 관리할 정보.
object ThreadContextUtil {
    private val holder: ThreadLocal<ThreadContext> = ThreadLocal<ThreadContext>()

    class ThreadContext(var xRequestId: String,
        var userId: Long?,
        var exception: Exception?) {
    }

    fun getXRequestId(): String {
        return getContext().xRequestId
    }

    fun setXRequestId(xRequestId: String) {
        getContext().xRequestId = xRequestId
    }

    fun getUserId(): Long? {
        return getContext().userId
    }

    fun setUserId(userId: Long) {
        getContext().userId = userId
    }

    fun getException(): Exception? {
        return getContext().exception
    }

    fun setException(exception: Exception) {
        getContext().exception = exception
    }

    fun removeAllThreadContexts() {
        holder.remove()
    }

    private fun getContext(): ThreadContext {
        return holder.get() ?: run {
            val threadContext = ThreadContext("IRUMA-" + UUID.randomUUID(), 0, null)
            holder.set(threadContext)
            return threadContext
        }
    }
}
