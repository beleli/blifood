package br.com.blifood.infrastructure.cache

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Duration

class MemoryAdapterTest : StringSpec({

    val objectMapper = mockk<ObjectMapper>()
    val memoryAdapter = MemoryAdapter(objectMapper)

    "should get value from Redis successfully" {
        val key = "test-key"
        val expectedValue = "test-value"
        val duration = Duration.ofMinutes(5)
        val resultClass = String::class.java

        every { objectMapper.writeValueAsString(expectedValue) } returns expectedValue
        every { objectMapper.readValue(expectedValue, resultClass) } returns expectedValue

        memoryAdapter.setKey(key, expectedValue, duration)
        val result = memoryAdapter.getKey(key, resultClass)

        result shouldBe expectedValue
        verify { objectMapper.readValue(expectedValue, resultClass) }
    }

    "should return null when key does not exist in Redis" {
        val key = "non-existent-key"
        val resultClass = String::class.java

        val result = memoryAdapter.getKey(key, resultClass)

        result shouldBe null
    }

    "should set value in Redis successfully with expiration" {
        val key = "test-key"
        val value = "test-value"
        val duration = Duration.ofMinutes(5)

        every { objectMapper.writeValueAsString(value) } returns value

        memoryAdapter.setKey(key, value, duration)

        verify { objectMapper.writeValueAsString(value) }
    }
})
