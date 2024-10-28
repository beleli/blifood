package br.com.blifood.api.exceptionhandler

import br.com.blifood.api.exceptionhandler.ApiProblemDetail.ApiFieldError
import br.com.blifood.api.v1.getRequestContextHolderUserId
import br.com.blifood.core.log.compactJson
import br.com.blifood.core.log.toJsonLog
import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.exception.UserNotAuthorizedException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.micrometer.tracing.Tracer
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.util.ContentCachingRequestWrapper
import java.net.URI

@ControllerAdvice
class ExceptionHandler(
    @Value("\${blifood.logging.invalid-requests}")
    private val logInvalidRequest: Boolean,
    private val tracer: Tracer
) : ResponseEntityExceptionHandler() {

    private val apiLogger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(Exception::class)
    fun handleUncaughtException(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        apiLogger.error(ex.message, ex)
        return this.handleExceptionInternal(ex, Messages.get("system.unscathedException"), HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    @ExceptionHandler(PropertyReferenceException::class)
    fun handlePropertyReferenceException(ex: PropertyReferenceException, request: WebRequest): ResponseEntity<Any>? {
        return this.handleExceptionInternal(ex, Messages.get("system.propertyReferenceException"), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any>? {
        return this.handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<Any>? {
        return this.handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(UserNotAuthorizedException::class)
    fun handleUserNotAuthorizedException(ex: UserNotAuthorizedException, request: WebRequest): ResponseEntity<Any>? {
        return this.handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException, request: WebRequest): ResponseEntity<Any>? {
        return this.handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<Any>? {
        val errors = mutableSetOf<ApiFieldError>()
        ex.constraintViolations.map {
            errors.add(ApiFieldError(it.propertyPath.toString(), Messages.get(it.message)))
        }
        val problemDetail = buildProblem(HttpStatus.BAD_REQUEST, Messages.get("validation.failed"), request, errors)
        return this.handleExceptionInternal(ex, problemDetail, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        apiLogger.logRequest(request)
        val errors = mutableSetOf<ApiFieldError>()
        ex.bindingResult.allErrors.map {
            when (it) {
                is FieldError -> errors.add(ApiFieldError(it.field, Messages.get(it.defaultMessage!!)))
                is ObjectError -> errors.add(ApiFieldError(it.objectName, Messages.get(it.defaultMessage!!)))
                else -> {}
            }
        }
        val problemDetail = buildProblem(HttpStatus.BAD_REQUEST, message = Messages.get("validation.failed"), request, errors)
        return this.handleExceptionInternal(ex, problemDetail, headers, status, request)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        apiLogger.logRequest(request)
        val errors = mutableSetOf<ApiFieldError>()
        val rootCause = ex.rootCause
        when (rootCause) {
            is UnrecognizedPropertyException -> errors.add(ApiFieldError(rootCause.propertyName, Messages.get("unrecognized.field")))
            else -> {}
        }
        val problemDetail = buildProblem(HttpStatus.BAD_REQUEST, message = Messages.get("readRequest.failed"), request, errors)
        return this.handleExceptionInternal(Exception(rootCause), problemDetail, headers, status, request)
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val newBody = when (body) {
            null -> buildProblem(statusCode, ex.localizedMessage, request)
            is String -> buildProblem(statusCode, body, request)
            else -> body
        }
        apiLogger.logErrorResponse(statusCode.value(), newBody)
        return super.handleExceptionInternal(ex, newBody, headers, statusCode, request)
    }

    private fun buildProblem(
        statusCode: HttpStatusCode,
        message: String,
        request: WebRequest,
        fieldErrors: Set<ApiFieldError>? = null
    ): ApiProblemDetail {
        return ApiProblemDetail(
            title = HttpStatus.valueOf(statusCode.value()).reasonPhrase,
            status = statusCode.value(),
            detail = message,
            instance = URI.create((request as ServletWebRequest).request.requestURI),
            traceId = tracer.currentTraceContext().context()?.traceId(),
            errors = fieldErrors
        )
    }

    private fun Logger.logErrorResponse(status: Int, response: Any) {
        this.error("response httpStatus:$status, body:${response.toJsonLog()}")
    }

    private fun getRequestBody(request: WebRequest): String {
        if (!logInvalidRequest) return "not logged"
        val nativeRequest = (request as ServletWebRequest).nativeRequest as ContentCachingRequestWrapper
        return String(nativeRequest.contentAsByteArray).compactJson()
    }

    private fun Logger.logRequest(request: WebRequest) {
        request as ServletWebRequest
        this.info(
            "request userId:${getRequestContextHolderUserId()}, uri:${request.request.requestURI}, " +
                "httpMethod:${request.request.method}, body:${getRequestBody(request)}"
        )
    }
}
