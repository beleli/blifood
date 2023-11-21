package br.com.blifood.core.security

import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyStore
import java.security.PublicKey

@Component
class JwtKeyProvider(private val properties: JwtKeyStoreProperties) {

    private val keyStore: KeyStore = loadKeyStore()
    private val key: Key = loadKey()
    private val publicKey: PublicKey = loadPublicKey()

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

    fun getKey() = key
    fun getPublicKey() = publicKey
}
