package br.com.blifood.core.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.OffsetDateTime
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class MaskProperty(val format: LogMaskFormat = LogMaskFormat.DEFAULT)

enum class LogMaskFormat {
    DEFAULT,
    CPF,
    ADDRESS,
    EMAIL,
    NAME
}

private const val MASK_MAX_LENGTH = 5
private const val APPLICATION_PACKAGED = "br.com.blifood"

fun Any.toLog(): String {
    val propertiesMap = this.getProperties { it?.toLog() }
    val propertiesString = propertiesMap.map { (prop, value) -> "${prop.name}=$value" }.joinToString(",")
    return "${this::class.simpleName}($propertiesString)"
}

fun Any.toJsonLog(): String {
    val propertiesMap = this.getProperties { it?.toJsonLog() }
    val propertiesJsonMap = propertiesMap.map { (prop, value) -> prop.name to value }.toMap()
    return jacksonObjectMapper().registerKotlinModule().writeValueAsString(propertiesJsonMap)
        .replace("\"{", "{")
        .replace("}\"", "}")
        .replace("\\\"", "\"")
}

private fun Any.getProperties(logFunction: (Any?) -> String?): Map<KProperty1<out Any, *>, Any?> {
    val propertiesMap = this::class.declaredMemberProperties
        .associateWith { prop ->
            val value = prop.getter.call(this)
            when {
                value is Iterable<*> -> value.map { logFunction(it) }
                value?.javaClass?.name?.startsWith(APPLICATION_PACKAGED) == true -> {
                    if (prop.returnType.isSubtypeOf(Enum::class.starProjectedType)) {
                        value.toString()
                    } else {
                        logFunction(value)
                    }
                }
                prop.annotations.any { it is MaskProperty } -> {
                    value.toString().applyMask(prop.findAnnotation<MaskProperty>()!!.format)
                }
                value is OffsetDateTime -> value.toString()
                else -> value
            }
        }
    return propertiesMap
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
    return if (this.length < MASK_MAX_LENGTH) "*".repeat(this.length) else "*".repeat(MASK_MAX_LENGTH)
}

fun String.maskCPF(): String {
    return this.take(3) + MASK_MAX_LENGTH + this.takeLast(2)
}

fun String.maskEmail(): String {
    return this.replace("(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*\\.)".toRegex()) { "*" }
}

fun String.maskAfter(lastDigit: Int): String {
    return if (this.length > lastDigit) {
        if (this.length > lastDigit + MASK_MAX_LENGTH) {
            this.replaceRange(lastDigit, length, "*".repeat(MASK_MAX_LENGTH))
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
    val result = java.lang.StringBuilder(this.length)
    var i = 0
    while (i < this.length) {
        val ch = this[i]
        when {
            ch == '\u0000' || ch == '\\' -> {
                i++
                continue
            }
            ch == '"' && i < this.length - 1 && this[i + 1] == '{' -> {
                result.append('{')
                i++
            }
            ch == '}' && i < this.length - 1 && this[i + 1] == '"' -> {
                result.append('}')
                i++
            }
            else -> {
                result.append(ch)
            }
        }
        i++
    }
    return result.toString()
}
