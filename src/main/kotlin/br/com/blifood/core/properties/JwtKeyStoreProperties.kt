package br.com.blifood.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties("blifood.jwt-keystore")
data class JwtKeyStoreProperties(
    val jksLocation: Resource,
    val keypairAlias: Resource,
    val password: Resource
)
