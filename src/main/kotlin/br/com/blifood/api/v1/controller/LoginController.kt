package br.com.blifood.api.v1.controller

import br.com.blifood.api.log.LogAndValidate
import br.com.blifood.api.security.JwtKeyProvider
import br.com.blifood.api.v1.model.LoginModel
import br.com.blifood.api.v1.model.input.LoginInputModel
import br.com.blifood.api.v1.openapi.LoginControllerOpenApi
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.exception.UserNotAuthorizedException
import br.com.blifood.domain.exception.UserNotFoundException
import br.com.blifood.domain.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/login", produces = [MediaType.APPLICATION_JSON_VALUE])
class LoginController(
    private val userService: UserService,
    private val jwtKeyProvider: JwtKeyProvider
) : LoginControllerOpenApi {

    @LogAndValidate(logResponse = false)
    @PostMapping
    override fun login(
        @RequestBody
        loginInputModel: LoginInputModel
    ): LoginModel {
        try {
            val user = userService.validateLogin(loginInputModel.username!!, loginInputModel.password!!)
            return LoginModel(generateToken(user.email, user.id, user.profile))
        } catch (ex: UserNotFoundException) {
            throw UserNotAuthorizedException()
        } catch (ex: UserNotAuthorizedException) {
            throw UserNotAuthorizedException()
        } catch (ex: Throwable) {
            throw ex
        }
    }

    private fun generateToken(email: String, id: Long, type: UserProfile): String {
        val date = Date()
        val expiration = Date(date.time + 3600000)

        val headers: MutableMap<String, Any> = mutableMapOf()
        headers["typ"] = "JWT"

        val claims: MutableMap<String, Any> = mutableMapOf()
        claims["user_id"] = id
        claims["scopes"] = listOf("READ", "WRITE")
        claims["authorities"] = type.permissions.sortedBy { it.authority }.map { it.authority }

        return Jwts.builder()
            .setHeader(headers)
            .setSubject(email)
            .addClaims(claims)
            .setIssuedAt(date)
            .setExpiration(expiration)
            .signWith(jwtKeyProvider.getKey(), SignatureAlgorithm.RS256)
            .compact()
    }
}
