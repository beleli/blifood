package br.com.blifood.api.v1

import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

fun addUriInResponseHeader(resourceId: Any) {
    val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{resourceId}")
        .buildAndExpand(resourceId).toUri()
    val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.response
    response!!.setHeader(HttpHeaders.LOCATION, uri.toString())
}

fun getSecurityContextHolderUserId(): Long {
    val details: Map<String, Any> = SecurityContextHolder.getContext().authentication.details as Map<String, Any>
    return details["user_id"].toString().toLong()
}
