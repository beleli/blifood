package br.com.blifood.core.secretsmanager

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class SecretsManager(
    private val secretsManagerClient: SecretsManagerClient
) {

    fun getValue(secretName: String): String {
        val valueRequest = GetSecretValueRequest.builder().secretId(secretName).build()
        val response = secretsManagerClient.getSecretValue(valueRequest)
        return response.secretString()
    }
}
