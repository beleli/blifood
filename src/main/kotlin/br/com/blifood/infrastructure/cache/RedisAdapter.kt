package br.com.blifood.infrastructure.cache

import br.com.blifood.domain.service.CacheService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

class RedisAdapter(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) : CacheService {

    override fun <T> getKey(key: String, clazz: Class<T>): T? {
        val valueOps = redisTemplate.opsForValue()
        return valueOps.get(key)?.let { objectMapper.readValue(it.toString(), clazz) }
    }

    override fun setKey(key: String, value: Any, duration: Duration) {
        val valueOps = redisTemplate.opsForValue()
        valueOps.set(key, objectMapper.writeValueAsString(value), duration)
    }
}
