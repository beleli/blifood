package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.UserController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.input.ChangeProfileInputModel
import br.com.blifood.api.v1.model.input.UserChangePasswordModel
import br.com.blifood.api.v1.model.input.UserInputModel
import br.com.blifood.api.v1.model.input.UserWithPasswordInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.service.UserService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify

class UserControllerTest : StringSpec({
    val userService = mockk<UserService>()
    val controller = UserController(userService)

    val user = createUser()
    val userId = user.id
    val requesterId = 2L
    val newPassword = "newPassword"

    fun createUserInputModel(user: User) = UserInputModel(user.name, user.email)
    fun createUserChangePasswordModel(user: User) = UserChangePasswordModel(user.password, newPassword)
    fun createUserWithPasswordInputModel(user: User) = UserWithPasswordInputModel(user.name, user.email, user.password)
    fun createChangeProfileInputModel(profile: UserProfile) = ChangeProfileInputModel(profile.toString())

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns requesterId

    "findById should return UserModel" {
        every { userService.findOrThrow(any()) } returns user

        val result = controller.findById(userId)
        result shouldBe user.toModel()
    }

    "create should return created UserModel" {
        every { userService.save(any()) } returns user

        val result = controller.create(createUserWithPasswordInputModel(user))
        result shouldBe user.toModel()
    }

    "alter should return altered UserModel" {
        every { userService.findOrThrow(any()) } returns user
        every { userService.save(any()) } returns user

        controller.alter(userId, createUserInputModel(user))
        verify { userService.save(user) }
    }

    "changePassword should return NO_CONTENT" {
        every { userService.changePassword(any(), any(), any()) } answers {}

        controller.changePassword(userId, createUserChangePasswordModel(user))
        verify { userService.changePassword(userId, user.password, newPassword) }
    }

    "changeProfile should return NO_CONTENT" {
        every { userService.changeProfile(any(), any(), any()) } answers {}

        controller.changeProfile(userId, createChangeProfileInputModel(UserProfile.ADMIN))
        verify { userService.changeProfile(requesterId, userId, UserProfile.ADMIN) }
    }
})
