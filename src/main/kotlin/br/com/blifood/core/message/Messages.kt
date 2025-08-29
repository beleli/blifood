package br.com.blifood.core.message

import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import java.text.MessageFormat
import java.util.*

object Messages {

    private const val BUNDLE_BASE_NAME = "i18n/messages"
    private val defaultBundle: ResourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME)
    private val enBundle: ResourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, Locale("en"))
    private val ptBundle: ResourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, Locale("pt"))

    fun get(key: String, vararg args: Any?): String {
        val message = getMessageFromBundle(key)
        return if (args.isNotEmpty()) {
            MessageFormat.format(message, *args)
        } else {
            message
        }
    }

    fun get(key: String, args: Map<String, Any?>): String {
        var message = getMessageFromBundle(key)
        if (args.isNotEmpty()) {
            for ((k, v) in args) {
                message = message.replace("{$k}", v.toString())
            }
        }
        return message
    }

    private fun getMessageFromBundle(key: String): String {
        val locale: Locale = LocaleContextHolder.getLocale()
        val bundle = when (locale.language.lowercase()) {
            "pt" -> ptBundle
            "en" -> enBundle
            else -> defaultBundle
        }

        return try {
            bundle.getString(key)
        } catch (_: NoSuchMessageException) {
            key
        } catch (_: MissingResourceException) {
            key
        } catch (ex: Exception) {
            throw ex
        }
    }
}
