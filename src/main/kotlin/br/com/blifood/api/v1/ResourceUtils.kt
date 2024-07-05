package br.com.blifood.api.v1

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.reflect.KClass

const val DEFAULT_PAGE_SIZE: Int = 10

fun addUriInResponseHeader(resourceId: Any) {
    val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{resourceId}")
        .buildAndExpand(resourceId).toUri()
    val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.response
    response!!.setHeader(HttpHeaders.LOCATION, uri.toString())
}

fun getRequestContextHolderUserId(): Long {
    val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
    return request.getUserId()
}

fun HttpServletRequest.getUserId(): Long {
    val userId = this.getAttribute("userId")
    return if (userId is Int) userId.toLong() else 0L
}

fun <T : Annotation> JoinPoint.getAnnotation(annotation: KClass<T>): T? {
    val methodSignature = this.signature as? MethodSignature ?: return null
    return methodSignature.method.getAnnotation(annotation.java)
}
