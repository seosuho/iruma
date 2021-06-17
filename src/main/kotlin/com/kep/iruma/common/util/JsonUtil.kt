package com.kep.iruma.common.util

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.kep.iruma.common.exception.IrumaRuntimeException
import com.kep.iruma.common.exception.ResultCode
import java.io.File
import java.io.InputStream
import java.io.Writer

object JsonUtil {
    private val objectMapper: ObjectMapper =
        ObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule())

    fun getJacksonObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    fun toJsonString(obj: Any): String {
        return try {
            objectMapper.writeValueAsString(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun toMap(str: String): Map<String, Any?> {
        val typeReference: TypeReference<Map<String, Any?>> = object : TypeReference<Map<String, Any?>>() {}
        return toObject(str, typeReference)
    }

    fun toMap(file: File?): Map<String, Any?>? {
        val typeReference: TypeReference<Map<String, Any?>> = object : TypeReference<Map<String, Any?>>() {}
        return toObject(file, typeReference)
    }

    fun toObject(bytes: ByteArray): Any? {
        try {
            if (bytes.isEmpty()) {
                return null
            }
            return objectMapper.readValue(bytes, Any::class.java)

        } catch (e: Exception) {
            throw RuntimeException("Can't make byte array to object")
        }
    }

    fun <T> toObject(str: String?, clazz: Class<T>): T {
        return try {
            objectMapper.readValue(str, clazz)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun <T> toObject(str: String, typeReference: TypeReference<T>): T {
        return try {
            objectMapper.readValue(str, typeReference)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun <T> toObject(inputStream: InputStream, clazz: Class<T>): T {
        return try {
            objectMapper.readValue(inputStream, clazz)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun <T> toObject(inputStream: InputStream, typeReference: TypeReference<T>): T {
        return try {
            objectMapper.readValue(inputStream, typeReference)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun <T> toObject(file: File?, typeReference: TypeReference<T>): T {
        return try {
            objectMapper.readValue(file, typeReference)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun <T> toObject(str: String, type: JavaType): T {
        return try {
            objectMapper.readValue(str, type)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun write(writer: Writer?, obj: Any?) {
        try {
            objectMapper.writeValue(writer, obj)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun toJsonNode(str: String): JsonNode {
        return try {
            objectMapper.readTree(str)
        } catch (e: Exception) {
            throw IrumaRuntimeException(ResultCode.UNEXPECTED_ERROR)
        }
    }

    fun getType(clazz: Class<*>, subClazzz: Class<*>): JavaType? {
        return objectMapper.getTypeFactory().constructCollectionLikeType(clazz, subClazzz)
    }
}