package br.com.blifood.api.v1

import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

object ResourceUriHelper {
    fun addUriInResponseHeader(resourceId: Any) {
        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(resourceId).toUri()
        val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?)!!.response
        response!!.setHeader(HttpHeaders.LOCATION, uri.toString())
    }
}
