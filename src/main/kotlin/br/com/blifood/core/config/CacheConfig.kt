package br.com.blifood.core.config

import br.com.blifood.core.properties.CacheProperties
import br.com.blifood.domain.service.CacheService
import br.com.blifood.infrastructure.cache.MemoryAdapter
import br.com.blifood.infrastructure.cache.RedisAdapter
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class CacheConfig(
    private val cacheProperties: CacheProperties,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun cacheService(): CacheService {
        return when (cacheProperties.type) {
            CacheProperties.Type.REDIS -> RedisAdapter(redisTemplate(redisConnectionFactory()), objectMapper)
            CacheProperties.Type.MEMORY -> MemoryAdapter(objectMapper)
        }
    }

    @ConditionalOnProperty(name = ["blifood.cache.type"], havingValue = "redis")
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(cacheProperties.redis!!.host, cacheProperties.redis.port)
    }

    @ConditionalOnProperty(name = ["blifood.cache.type"], havingValue = "redis")
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory
        return template
    }
}
