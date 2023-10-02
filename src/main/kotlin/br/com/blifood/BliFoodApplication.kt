package br.com.blifood

import br.com.blifood.core.email.EmailProperties
import br.com.blifood.core.storage.StorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(StorageProperties::class, EmailProperties::class)
@SpringBootApplication
class BliFoodApplication

fun main(args: Array<String>) {
    runApplication<BliFoodApplication>(*args)
}
