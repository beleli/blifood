package br.com.blifood.api.v1

import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

const val DEFAULT_PAGE_SIZE: Int = 10

fun addUriInResponseHeader(resourceId: Any) {
    val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{resourceId}")
        .buildAndExpand(resourceId).toUri()
    val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.response
    response!!.setHeader(HttpHeaders.LOCATION, uri.toString())
}

fun getSecurityContextHolderUserId(): Long {
    val details = SecurityContextHolder.getContext().authentication.details
    return if (details is Map<*, *>) details["user_id"].toString().toLong() else 0
}
