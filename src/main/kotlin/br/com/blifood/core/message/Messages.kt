package br.com.blifood.core.message

import org.springframework.context.NoSuchMessageException
import java.util.*

object Messages {
    private val resourceBundle = ResourceBundle.getBundle("i18n/messages")
    fun get(key: String, vararg args: Any?): String {
        return try {
            if (args.isEmpty()) {
                resourceBundle.getString(key)
            } else {
                resourceBundle.getString(key).format(*args)
            }
        } catch (ex: NoSuchMessageException) {
            key
        } catch (ex: MissingResourceException) {
            key
        } catch (ex: Exception) {
            throw ex
        }
    }
}
