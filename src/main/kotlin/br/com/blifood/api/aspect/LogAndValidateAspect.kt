package br.com.blifood.api.aspect

import br.com.blifood.api.v1.getUserId
import br.com.blifood.core.log.toJsonLog
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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class LogAndValidate(
    val validateRequest: Boolean = true,
    val logResponse: Boolean = true,
    vararg val validationGroups: KClass<*> = []
)

@Aspect
@Component
class LogAndValidateAspect {

    private val loggers = mutableMapOf<Class<*>, Logger>()
    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Pointcut("@annotation(logAnnotation)")
    fun logMethods(logAnnotation: LogAndValidate) {
    }

    @Before("logMethods(logAnnotation)")
    fun logRequest(joinPoint: JoinPoint, logAnnotation: LogAndValidate) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val requestBody = getRequestBody(joinPoint)
        if (requestBody == null) {
            val parameter = joinPoint.args.firstOrNull()
            val log = if (parameter is Pageable) parameter else "body:null"
            getLogger(joinPoint).info("request userId:${request.getUserId()}, uri:${request.requestURI}, httpMethod:${request.method}, $log}")
        } else {
            getLogger(joinPoint).info("request userId:${request.getUserId()}, uri:${request.requestURI}, httpMethod:${request.method}, body:${requestBody.toJsonLog()}")
            if (logAnnotation.validateRequest) validateRequestBody(requestBody, *logAnnotation.validationGroups)
        }
    }

    @AfterReturning(value = "logMethods(logAnnotation)", returning = "returnValue")
    fun logResponse(joinPoint: JoinPoint, logAnnotation: LogAndValidate, returnValue: Any?) {
        val status = if (returnValue is ResponseEntity<*>) returnValue.statusCode.value() else getResponseStatus(joinPoint)
        val body = if (returnValue is ResponseEntity<*>) returnValue.body else returnValue
        getLogger(joinPoint).info("response httpStatus:$status, body:${if (logAnnotation.logResponse) body?.toJsonLog() else "not logged"}")
    }

    private fun getLogger(joinPoint: JoinPoint): Logger {
        val resourceClass = joinPoint.target.javaClass
        return loggers.getOrPut(resourceClass) { LoggerFactory.getLogger(resourceClass) }
    }

    private fun getResponseStatus(joinPoint: JoinPoint): Int {
        val methodSignature = joinPoint.signature as MethodSignature
        val responseStatusAnnotation = methodSignature.method.getAnnotation(ResponseStatus::class.java)
        return responseStatusAnnotation?.value?.value() ?: HttpStatus.OK.value()
    }

    private fun getRequestBody(joinPoint: JoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val parameters = methodSignature.method.parameters
        for ((index, parameter) in parameters.withIndex()) {
            if (parameter.isAnnotationPresent(RequestBody::class.java)) {
                return joinPoint.args[index]
            }
        }
        return null
    }

    private fun validateRequestBody(requestBody: Any, vararg validationGroups: KClass<*>) {
        val violations = validator.validate(requestBody, *validationGroups.map { it.java }.toTypedArray())
        if (violations.isNotEmpty()) throw ConstraintViolationException(violations)
    }
}
