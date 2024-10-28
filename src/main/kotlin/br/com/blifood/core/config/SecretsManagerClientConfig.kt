package br.com.blifood.core.config

import br.com.blifood.core.properties.SecretsManagerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import java.net.URI

@Configuration
class SecretsManagerClientConfig(
    private val secretsManagerProperties: SecretsManagerProperties
) {

    companion object {
        fun createWithImpl(impl: SecretsManagerProperties.Impl): SecretsManagerClientConfig {
            return SecretsManagerClientConfig(SecretsManagerProperties(impl))
        }
    }

    @Bean
    fun secretsManager(): SecretsManagerClient {
        return when (secretsManagerProperties.impl) {
            SecretsManagerProperties.Impl.AWS -> getAwsImpl()
            SecretsManagerProperties.Impl.LOCALSTACK -> getLocalstackImpl()
        }
    }

    private fun getAwsImpl(): SecretsManagerClient {
        return SecretsManagerClient.create()
    }

    private fun getLocalstackImpl(): SecretsManagerClient {
        return SecretsManagerClient.builder()
            .endpointOverride(URI.create("http://localhost:4566"))
            .region(Region.of("us-east-1"))
            .credentialsProvider { AwsBasicCredentials.create("localstack", "localstack") }
            .build()
    }
}

fun SecretsManagerClient.getSecretValue(secretName: String): String {
    val valueRequest = GetSecretValueRequest.builder().secretId(secretName).build()
    val response = this.getSecretValue(valueRequest)
    return response.secretString()
}
