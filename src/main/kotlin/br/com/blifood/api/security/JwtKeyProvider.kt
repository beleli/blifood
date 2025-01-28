package br.com.blifood.api.security

import br.com.blifood.core.properties.JwtKeyStoreProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyStore
import java.security.PublicKey

@Component
@EnableConfigurationProperties(JwtKeyStoreProperties::class)
class JwtKeyProvider(private val properties: JwtKeyStoreProperties) {

    private val keyStore: KeyStore = loadKeyStore()
    val key: Key = loadKey()
    val publicKey: PublicKey = loadPublicKey()

    private fun loadKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance("JKS")
        keyStore.load(properties.jksLocation.inputStream, properties.password.toCharArray())
        return keyStore
    }

    private fun loadKey(): Key {
        return keyStore.getKey(properties.keypairAlias, properties.password.toCharArray())
    }

    private fun loadPublicKey(): PublicKey {
        return keyStore.getCertificate(properties.keypairAlias).publicKey
    }
}
