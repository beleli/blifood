package br.com.blifood.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import java.net.URI

@Configuration
class SecretsManagerClientConfig {

    @Bean
    @Profile("!local & !test")
    fun secretsManager(): SecretsManagerClient {
        return SecretsManagerClient.create()
    }

    @Profile("local || test")
    @Bean
    fun secretsManagerLocal(): SecretsManagerClient {
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
