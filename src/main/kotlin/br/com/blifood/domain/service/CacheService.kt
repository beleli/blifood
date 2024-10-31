package br.com.blifood.domain.service

import java.time.Duration

interface CacheService {
    fun <T> getKey(key: String, clazz: Class<T>): T?
    fun setKey(key: String, value: Any, duration: Duration)
}
