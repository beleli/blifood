package br.com.blifood.core.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Pageable
import java.time.OffsetDateTime
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class MaskObject

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class MaskProperty(val format: LogMaskFormat = LogMaskFormat.DEFAULT)

enum class LogMaskFormat {
    DEFAULT,
    CPF,
    ADDRESS,
    EMAIL,
    NAME
}

private const val MASK_LENGTH = 5

fun Any.toLog(): String {
    val propertiesMap = this::class.declaredMemberProperties
        .associateWith { prop ->
            val value = prop.getter.call(this)
            if (prop.annotations.any { it is MaskObject || it is Pageable }) {
                if (value is Iterable<*>) value.map { it?.toLog() } else value?.toLog()
            } else if (prop.annotations.any { it is MaskProperty }) {
                value.toString().applyMask(prop.findAnnotation<MaskProperty>()!!.format)
            } else {
                value
            }
        }

    val propertiesString = propertiesMap.map { (prop, value) ->
        "${prop.name}=$value"
    }.joinToString(", ")

    return "${this::class.simpleName}($propertiesString)"
}

fun Any.toJsonLog(): String {
    val propertiesMap = this::class.declaredMemberProperties
        .associateWith { prop ->
            val value = prop.getter.call(this)
            if (prop.annotations.any { it is MaskObject }) {
                if (value is Iterable<*>) value.map { it?.toJsonLog() } else value?.toJsonLog()
            } else if (prop.annotations.any { it is MaskProperty }) {
                value.toString().applyMask(prop.findAnnotation<MaskProperty>()!!.format)
            } else {
                if (value is OffsetDateTime) value.toString() else value
            }
        }

    val propertiesJsonMap = propertiesMap.map { (prop, value) ->
        prop.name to value
    }.toMap()

    return jacksonObjectMapper().writeValueAsString(propertiesJsonMap)
        .replace("\"{", "{")
        .replace("}\"", "}")
        .replace("\\\"", "\"")
}

fun String?.applyMask(format: LogMaskFormat): String? {
    if (this == null) return null
    return when (format) {
        LogMaskFormat.DEFAULT -> maskAll()
        LogMaskFormat.CPF -> maskCPF()
        LogMaskFormat.ADDRESS -> maskAfter(5)
        LogMaskFormat.EMAIL -> maskEmail()
        LogMaskFormat.NAME -> maskName()
    }
}

fun String.maskAll(): String {
    return if (this.length < MASK_LENGTH) {
        "*".repeat(this.length)
    } else {
        "*".repeat(MASK_LENGTH)
    }
}

fun String.maskCPF(): String {
    return this.take(3) + MASK_LENGTH + this.takeLast(2)
}

fun String.maskEmail(): String {
    return this.replace("(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*\\.)".toRegex()) { "*" }
}

fun String.maskAfter(lastDigit: Int): String {
    return if (this.length > lastDigit) {
        if (this.length > lastDigit + MASK_LENGTH) {
            this.replaceRange(lastDigit, length, "*".repeat(MASK_LENGTH))
        } else {
            this.replaceRange(lastDigit, length, "*".repeat(length - lastDigit))
        }
    } else {
        this
    }
}

fun String.maskName(): String {
    return this.split(" ").map { it.maskAfter(2) }.reduce { acc, s -> "$acc $s" }
}

fun String.compactJson(): String {
    var isInQuotes = false
    val result = StringBuilder()
    for (char in this) {
        when (char) {
            '"' -> isInQuotes = !isInQuotes
            ' ', '\r', '\n', '\t' -> if (!isInQuotes) continue
        }
        result.append(char)
    }
    return result.toString()
}
