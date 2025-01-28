package br.com.blifood.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blifood.email")
class EmailProperties(
    val impl: Impl,
    val from: String,
    val to: Set<String>?
) {
    init {
        if (impl == Impl.SANDBOX) {
            require(to != null) { "blifood.email.to configuration can not be null" }
        }
    }

    enum class Impl {
        SMTP,
        FAKE,
        SANDBOX
    }
}
