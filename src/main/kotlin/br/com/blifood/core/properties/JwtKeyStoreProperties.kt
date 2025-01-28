package br.com.blifood.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource
import java.nio.charset.Charset

@ConfigurationProperties("blifood.jwt-keystore")
class JwtKeyStoreProperties(jksLocation: Resource, keypairAlias: Resource, password: Resource) {
    val jksLocation: Resource = jksLocation
    val keypairAlias: String = keypairAlias.getContentAsString(Charset.defaultCharset())
    val password: String = password.getContentAsString(Charset.defaultCharset())
}
