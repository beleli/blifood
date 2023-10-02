package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.model.UserModel
import br.com.blifood.api.v1.model.input.UserChangePasswordModel
import br.com.blifood.api.v1.model.input.UserInputModel
import br.com.blifood.api.v1.model.input.UserWithPasswordInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.UserControllerOpenApi
import br.com.blifood.domain.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    private val userService: UserService
) : UserControllerOpenApi {

    @GetMapping("/{userId}")
    override fun findById(@PathVariable userId: Long): UserModel {
        return userService.findOrThrow(userId).toModel()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @RequestBody @Valid
        userWithPasswordInputDto: UserWithPasswordInputModel
    ): UserModel {
        return userService.save(userWithPasswordInputDto.toEntity()).toModel()
    }

    @PutMapping("/{userId}")
    override fun alter(
        @PathVariable userId: Long,
        @Valid @RequestBody
        userInputDto: UserInputModel
    ): UserModel {
        val user = userService.findOrThrow(userId).applyModel(userInputDto)
        return userService.save(user).toModel()
    }

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun changePassword(
        @PathVariable userId: Long,
        @Valid @RequestBody
        userChangePasswordDto: UserChangePasswordModel
    ) {
        userService.changePassword(userId, userChangePasswordDto.password!!, userChangePasswordDto.newPassword!!)
    }
}
