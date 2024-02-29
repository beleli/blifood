package br.com.blifood.api.security

import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtAuthenticationFilter(private val jwtKeyProvider: JwtKeyProvider) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        runCatching {
            SecurityContextHolder.getContext().authentication = getAuthentication(request as HttpServletRequest)
            filterChain.doFilter(request, response)
        }.onFailure {
            response as HttpServletResponse
            response.status = HttpStatus.FORBIDDEN.value()
        }
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val header = request.getHeader("Authorization") ?: return null
        val token = header.removePrefix("Bearer").trim()

        val claimsJws = Jwts.parserBuilder()
            .setSigningKey(jwtKeyProvider.getPublicKey())
            .build()

        val userName = claimsJws.parseClaimsJws(token).body.subject
        val authorities = (claimsJws.parseClaimsJws(token).body["authorities"] as ArrayList<*>)
            .map { SimpleGrantedAuthority(it.toString()) }

        val authentication = UsernamePasswordAuthenticationToken(userName, null, authorities)
        val userId = claimsJws.parseClaimsJws(token).body["user_id"]
        userId.let { authentication.details = mapOf("user_id" to userId) }

        return authentication
    }
}
