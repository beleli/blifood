package br.com.blifood.infrastructure.cache

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.Duration

class RedisAdapterTest : StringSpec({

    val redisTemplate = mockk<RedisTemplate<String, Any>>()
    val objectMapper = mockk<ObjectMapper>()
    val redisAdapter = RedisAdapter(redisTemplate, objectMapper)

    "should get value from Redis successfully" {
        val key = "test-key"
        val expectedValue = "test-value"
        val valueOps = mockk<ValueOperations<String, Any>>()
        val resultClass = String::class.java

        every { redisTemplate.opsForValue() } returns valueOps
        every { valueOps.get(key) } returns expectedValue
        every { objectMapper.readValue(expectedValue, resultClass) } returns expectedValue

        val result = redisAdapter.getKey(key, resultClass)

        result shouldBe expectedValue
        verify { valueOps.get(key) }
        verify { objectMapper.readValue(expectedValue, resultClass) }
    }

    "should return null when key does not exist in Redis" {
        val key = "non-existent-key"
        val valueOps = mockk<ValueOperations<String, Any>>()
        val resultClass = String::class.java

        every { redisTemplate.opsForValue() } returns valueOps
        every { valueOps.get(key) } returns null

        val result = redisAdapter.getKey(key, resultClass)

        result shouldBe null
        verify { valueOps.get(key) }
    }

    "should set value in Redis successfully with expiration" {
        val key = "test-key"
        val value = "test-value"
        val duration = Duration.ofMinutes(5)
        val valueOps = mockk<ValueOperations<String, Any>>()

        every { redisTemplate.opsForValue() } returns valueOps
        every { objectMapper.writeValueAsString(value) } returns value
        every { valueOps.set(key, value, duration) } just Runs

        redisAdapter.setKey(key, value, duration)

        verify { valueOps.set(key, value, duration) }
        verify { objectMapper.writeValueAsString(value) }
    }
})
