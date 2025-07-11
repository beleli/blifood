package br.com.blifood.core.message

import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

object Messages {

    fun get(key: String, vararg args: Any?): String {
        val resourceBundle = ResourceBundle.getBundle("i18n/messages", LocaleContextHolder.getLocale())
        return try {
            if (args.isEmpty()) {
                resourceBundle.getString(key)
            } else {
                resourceBundle.getString(key).format(*args)
            }
        } catch (_: NoSuchMessageException) {
            key
        } catch (_: MissingResourceException) {
            key
        } catch (ex: Exception) {
            throw ex
        }
    }
}
