package br.com.blifood.core.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.OffsetDateTime
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class MaskProperty(val format: LogMaskFormat = LogMaskFormat.DEFAULT)

enum class LogMaskFormat {
    DEFAULT, CPF, ADDRESS, EMAIL, NAME
}

interface Loggable {
    companion object {
        const val MASK_MAX_LENGTH = 5
    }

    fun toLog(): String {
        val propertiesMap = this.getProperties { it?.let { if (it is Loggable) it.toLog() else it.toString() } }
        val propertiesString = propertiesMap.map { (prop, value) -> "${prop.name}=$value" }.joinToString(",")
        return "${this::class.simpleName}($propertiesString)"
    }

    fun toJsonLog(): String {
        val propertiesMap = this.getProperties { it?.let { if (it is Loggable) it.toJsonLog() else it.toString() } }
        val propertiesJsonMap = propertiesMap.map { (prop, value) -> prop.name to value }.toMap()
        return jacksonObjectMapper().registerKotlinModule().writeValueAsString(propertiesJsonMap)
            .replace("\"{", "{")
            .replace("}\"", "}")
            .replace("\\\"", "\"")
    }

    private fun getProperties(logFunction: (Any?) -> String?): Map<KProperty1<out Loggable, *>, Any?> {
        val propertiesMap = this::class.declaredMemberProperties
            .associateWith { prop ->
                val value = prop.getter.call(this)
                when {
                    value is Iterable<*> -> value.map { logFunction(it) }
                    prop.annotations.any { it is MaskProperty } -> {
                        value.toString().applyMask(prop.findAnnotation<MaskProperty>()!!.format)
                    }
                    value is OffsetDateTime -> value.toString()
                    value is Loggable -> logFunction(value) // Apenas chama logFunction para objetos Loggable
                    else -> value
                }
            }
        return propertiesMap
    }

    private fun String?.applyMask(format: LogMaskFormat): String? {
        if (this == null) return null
        return when (format) {
            LogMaskFormat.DEFAULT -> maskAll()
            LogMaskFormat.CPF -> maskCPF()
            LogMaskFormat.ADDRESS -> maskAfter(5)
            LogMaskFormat.EMAIL -> maskEmail()
            LogMaskFormat.NAME -> maskName()
        }
    }

    private fun String.maskAll(): String {
        return if (this.length < MASK_MAX_LENGTH) "*".repeat(this.length) else "*".repeat(MASK_MAX_LENGTH)
    }

    private fun String.maskCPF(): String {
        return this.take(3) + MASK_MAX_LENGTH + this.takeLast(2)
    }

    private fun String.maskEmail(): String {
        return this.replace("(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*\\.)".toRegex()) { "*" }
    }

    private fun String.maskAfter(lastDigit: Int): String {
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

    private fun String.maskName(): String {
        return this.split(" ").map { it.maskAfter(2) }.reduce { acc, s -> "$acc $s" }
    }
}
