package br.com.blifood.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blifood.cache")
data class CacheProperties(
    val type: Type,
    val redis: Redis?
) {
    init {
        if (type == Type.REDIS) {
            require((redis != null)) { "blifood.cache.redis configuration can not be null" }
        }
    }

    enum class Type {
        REDIS,
        MEMORY
    }

    data class Redis(
        val host: String,
        val port: Int
    )
}
