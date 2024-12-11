package br.com.blifood.api.aspect

import br.com.blifood.api.v1.getUserId
import br.com.blifood.core.message.Messages
import br.com.blifood.domain.service.CacheService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.server.ResponseStatusException
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
annotation class RateLimit(
    val maxRequests: Int = 60,
    val timeValue: Long = 5,
    val timeUnit: TimeUnit = TimeUnit.MINUTES
)

@Aspect
@Component
class RateLimitAspect(
    private val cacheService: CacheService
) {

    @Pointcut("@annotation(rateLimit)")
    fun validateMethod(rateLimit: RateLimit) {
        // Pointcut definition
    }

    @Before("validateMethod(rateLimit)")
    fun checkRateLimit(joinPoint: JoinPoint, rateLimit: RateLimit) {
        val key = generateRateLimitKey()
        val periodInSeconds = rateLimit.timeUnit.toSeconds(rateLimit.timeValue)
        val currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val requestInfo = cacheService.getKey(key, RequestInfo::class.java)
        val validRequests = mutableListOf(currentTime)

        requestInfo?.let {
            validRequests.addAll(it.requests.filter { requestEpoch -> requestEpoch >= (currentTime - periodInSeconds) })
            if (validRequests.count() > rateLimit.maxRequests) {
                throw ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, Messages.get("api.rate-limit-exception"))
            }
        }
        cacheService.setKey(key, RequestInfo(validRequests), Duration.ofSeconds(periodInSeconds))
    }

    private fun generateRateLimitKey(): String {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val identification = request.getUserId().takeIf { it != 0L } ?: request.remoteAddr.replace(":", ".")
        return "rate_limit:$identification:${request.requestURI}:${request.method}"
    }

    private data class RequestInfo(val requests: MutableList<Long>)
}
