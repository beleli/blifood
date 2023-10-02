package br.com.blifood.core.email

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blifood.email")
data class EmailProperties(
    val impl: EmailImpl,
    val from: String,
    val to: Set<String>?
) {
    init {
        if (impl == EmailImpl.SANDBOX) {
            require(to != null) { "blifood.email.to configuration can not be null" }
        }
    }
}
