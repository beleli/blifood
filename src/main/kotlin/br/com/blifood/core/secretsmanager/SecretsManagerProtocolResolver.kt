package br.com.blifood.core.secretsmanager

import br.com.blifood.core.io.Base64ProtocolResolver
import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ProtocolResolver
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import java.net.URI

private const val SECRETS_MANAGER_PREFIX = "secretsManager:"

class SecretsManagerProtocolResolver : ProtocolResolver, ApplicationListener<ApplicationContextInitializedEvent> {

    private val secretsManager = SecretsManagerClient.builder()
        .endpointOverride(URI.create("http://localhost:4566"))
        .region(Region.of("us-east-1"))
        .credentialsProvider { AwsBasicCredentials.create("localstack", "localstack") }
        .build()

    private val base64ProtocolResolver = Base64ProtocolResolver()

    override fun resolve(location: String, resourceLoader: ResourceLoader): Resource? {
        if (location.startsWith(SECRETS_MANAGER_PREFIX)) {
            val key = location.removePrefix(SECRETS_MANAGER_PREFIX)
            val decodedResource = getValue(key)
            return base64ProtocolResolver.resolve(decodedResource, resourceLoader)
                ?: ByteArrayResource(decodedResource.toByteArray())
        }
        return null
    }

    override fun onApplicationEvent(event: ApplicationContextInitializedEvent) {
        event.applicationContext.addProtocolResolver(this)
    }

    private fun getValue(secretName: String): String {
        val valueRequest = GetSecretValueRequest.builder().secretId(secretName).build()
        val response = secretsManager.getSecretValue(valueRequest)
        return response.secretString()
    }
}
