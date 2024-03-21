package br.com.blifood.api.log

import br.com.blifood.core.log.toJsonLog
import br.com.blifood.core.log.toLog
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Target(AnnotationTarget.FUNCTION)
annotation class LogAndValidate(val validateRequest: Boolean = true, val logResponse: Boolean = true)

@Aspect
@Component
class LogAndValidateAspect {

    private val loggers = mutableMapOf<Class<*>, Logger>()

    @Pointcut("@annotation(logAnnotation)")
    fun logMethods(logAnnotation: LogAndValidate) {
    }

    @Before("logMethods(logAnnotation)")
    fun logRequest(joinPoint: JoinPoint, logAnnotation: LogAndValidate) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val requestBody = joinPoint.args.firstOrNull()
        if (requestBody is Pageable) {
            getLogger(joinPoint).info("request uri:${request.requestURI}, httpMethod:${request.method}, $requestBody")
        } else if (isRequestBody(joinPoint)) {
            getLogger(joinPoint).info("request uri:${request.requestURI}, httpMethod:${request.method}, body:${requestBody?.toLog()}")
            if (logAnnotation.validateRequest) validateRequestBody(requestBody)
        } else {
            getLogger(joinPoint).info("request uri:${request.requestURI}, httpMethod:${request.method}, body:null}")
        }
    }

    @AfterReturning(value = "logMethods(logAnnotation)", returning = "returnValue")
    fun logResponse(joinPoint: JoinPoint, logAnnotation: LogAndValidate, returnValue: Any?) {
        val status = getResponseStatus(joinPoint)
        getLogger(joinPoint).info("response httpStatus:$status, body:${if (logAnnotation.logResponse) returnValue?.toJsonLog() else "not logged"}")
    }

    private fun getLogger(joinPoint: JoinPoint): Logger {
        val resourceClass = joinPoint.target.javaClass
        return loggers.getOrPut(resourceClass) { LoggerFactory.getLogger(resourceClass) }
    }

    private fun getResponseStatus(joinPoint: JoinPoint): Int {
        val methodSignature = joinPoint.signature as MethodSignature
        val responseStatusAnnotation = methodSignature.method.getAnnotation(ResponseStatus::class.java)
        return responseStatusAnnotation?.value?.value() ?: 200
    }

    private fun isRequestBody(joinPoint: JoinPoint): Boolean {
        val methodSignature = joinPoint.signature as MethodSignature
        return methodSignature.method.parameters.any { it.isAnnotationPresent(RequestBody::class.java) }
    }

    private fun validateRequestBody(requestBody: Any?) {
        requestBody?.let {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val violations = validator.validate(it)
            if (violations.isNotEmpty()) throw ConstraintViolationException(violations)
        }
    }
}
