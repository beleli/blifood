package br.com.blifood.infrastructure.cache

import br.com.blifood.domain.service.CacheService
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class MemoryAdapter(
    private val objectMapper: ObjectMapper
) : CacheService {

    private val cache = mutableMapOf<String, Register>()

    override fun <T> getKey(key: String, clazz: Class<T>): T? {
        cleanExpiredRegisters()
        return cache[key]?.let { objectMapper.readValue(it.value.toString(), clazz) }
    }

    override fun setKey(key: String, value: Any, duration: Duration) {
        val expiration = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + duration.toSeconds()
        cache[key] = Register(objectMapper.writeValueAsString(value), expiration)
    }

    private fun cleanExpiredRegisters() {
        val now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        cache.entries.removeIf { it.value.expiration < now }
    }

    private data class Register(
        val value: Any,
        val expiration: Long
    )
}
