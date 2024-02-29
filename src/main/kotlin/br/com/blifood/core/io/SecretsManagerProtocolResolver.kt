package br.com.blifood.core.io

import br.com.blifood.core.config.SecretsManagerClientConfig
import br.com.blifood.core.config.getSecretValue
import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ProtocolResolver
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient

private const val SECRETS_MANAGER_PREFIX = "secretsmanager:"

class SecretsManagerProtocolResolver : ProtocolResolver, ApplicationListener<ApplicationContextInitializedEvent> {

    private val secretsManagerClient = setSecretsManagerClient()
    private val base64ProtocolResolver = Base64ProtocolResolver()

    override fun resolve(location: String, resourceLoader: ResourceLoader): Resource? {
        if (location.startsWith(SECRETS_MANAGER_PREFIX)) {
            val key = location.removePrefix(SECRETS_MANAGER_PREFIX)
            val decodedResource = secretsManagerClient.getSecretValue(key)
            return base64ProtocolResolver.resolve(decodedResource, resourceLoader)
                ?: ByteArrayResource(decodedResource.toByteArray())
        }
        return null
    }

    override fun onApplicationEvent(event: ApplicationContextInitializedEvent) {
        event.applicationContext.addProtocolResolver(this)
    }

    private fun setSecretsManagerClient(): SecretsManagerClient {
        val profiles = System.getenv()["spring.profiles.active"]?.lowercase()?.split(",")
        val isLocalProfile = profiles?.any { it == "local" || it == "test" } ?: true
        return if (isLocalProfile) {
            SecretsManagerClientConfig().secretsManagerLocal()
        } else {
            SecretsManagerClientConfig().secretsManager()
        }
    }
}
