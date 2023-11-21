package br.com.blifood

import br.com.blifood.core.email.EmailProperties
import br.com.blifood.core.io.Base64ProtocolResolver
import br.com.blifood.core.security.JwtKeyStoreProperties
import br.com.blifood.core.storage.StorageProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(StorageProperties::class, EmailProperties::class, JwtKeyStoreProperties::class)
@SpringBootApplication
class BliFoodApplication

fun main(args: Array<String>) {
    val app = SpringApplication(BliFoodApplication::class.java)
    app.addListeners(Base64ProtocolResolver())
    app.run(*args)
}
