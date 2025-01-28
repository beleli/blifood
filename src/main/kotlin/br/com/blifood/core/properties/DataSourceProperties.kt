package br.com.blifood.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource
import java.nio.charset.Charset

@ConfigurationProperties("blifood.datasource")
class DataSourceProperties(username: Resource, password: Resource) {
    val username: String = username.getContentAsString(Charset.defaultCharset())
    val password: String = password.getContentAsString(Charset.defaultCharset())
}
