package br.com.blifood.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blifood.secrets-manager")
class SecretsManagerProperties(
    val impl: Impl
) {
    enum class Impl {
        AWS,
        LOCALSTACK
    }
}
