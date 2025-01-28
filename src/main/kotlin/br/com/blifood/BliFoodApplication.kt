package br.com.blifood

import br.com.blifood.core.io.Base64ProtocolResolver
import br.com.blifood.core.io.SecretsManagerProtocolResolver
import br.com.blifood.core.io.StringProtocolResolver
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BliFoodApplication

fun main(args: Array<String>) {
    val app = SpringApplication(BliFoodApplication::class.java)
    app.addListeners(StringProtocolResolver(), Base64ProtocolResolver(), SecretsManagerProtocolResolver())
    app.run(*args)
}
