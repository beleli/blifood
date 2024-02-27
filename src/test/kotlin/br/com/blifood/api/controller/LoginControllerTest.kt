package br.com.blifood.api.controller

import br.com.blifood.api.v1.controller.LoginController
import br.com.blifood.api.v1.model.input.LoginInputModel
import br.com.blifood.core.security.JwtKeyProvider
import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.exception.UserNotAuthorizedException
import br.com.blifood.domain.exception.UserNotFoundException
import br.com.blifood.domain.service.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.security.KeyPair
import java.security.KeyPairGenerator

class LoginControllerTest : StringSpec({

    val userService = mockk<UserService>()
    val jwtKeyProvider = mockk<JwtKeyProvider>()
    val controller = LoginController(userService, jwtKeyProvider)

    val user = createUser()

    fun createLoginInputModel(user: User) = LoginInputModel(user.email, user.password)

    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }

    "login should return LoginModel with token" {
        every { userService.validateLogin(any(), any()) } returns user
        every { jwtKeyProvider.getKey() } returns generateKeyPair().private

        val result = controller.login(createLoginInputModel(user))
        result.token shouldNotBe null
    }

    "login should throw UserNotAuthorizedException when user not found" {
        every { userService.validateLogin(any(), any()) } throws UserNotFoundException()

        val exception = shouldThrow<UserNotAuthorizedException> { controller.login(createLoginInputModel(user)) }
        exception shouldBe UserNotAuthorizedException()
    }

    "login should throw UserNotAuthorizedException when user not authorized" {
        every { userService.validateLogin(any(), any()) } throws UserNotAuthorizedException()

        val exception = shouldThrow<UserNotAuthorizedException> { controller.login(createLoginInputModel(user)) }
        exception shouldBe UserNotAuthorizedException()
    }

    "login should throw Throwable when generic error" {
        every { userService.validateLogin(any(), any()) } throws Throwable()

        val exception = shouldThrow<Throwable> { controller.login(createLoginInputModel(user)) }
        exception shouldBe Throwable()
    }
})
