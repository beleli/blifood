package br.com.blifood.api.v1.controller

import br.com.blifood.api.aspect.LogAndValidate
import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.UserModel
import br.com.blifood.api.v1.model.input.ChangeProfileInputModel
import br.com.blifood.api.v1.model.input.UserChangePasswordModel
import br.com.blifood.api.v1.model.input.UserInputModel
import br.com.blifood.api.v1.model.input.UserWithPasswordInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.UserControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
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

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.USER_READ}')")
    @GetMapping("/{userId}")
    override fun findById(@PathVariable userId: Long): UserModel {
        return userService.findOrThrow(userId).toModel()
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.USER_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @RequestBody
        userWithPasswordInputModel: UserWithPasswordInputModel
    ): UserModel {
        return userService.save(userWithPasswordInputModel.toEntity()).toModel().also {
            addUriInResponseHeader(it.id)
        }
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.USER_WRITE}')")
    @PutMapping("/{userId}")
    override fun alter(
        @PathVariable userId: Long,
        @RequestBody
        userInputModel: UserInputModel
    ): UserModel {
        val user = userService.findOrThrow(userId).copy().applyModel(userInputModel)
        return userService.save(user).toModel()
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.USER_WRITE}')")
    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun changePassword(
        @PathVariable userId: Long,
        @RequestBody
        userChangePasswordModel: UserChangePasswordModel
    ) {
        userService.changePassword(userId, userChangePasswordModel.password!!, userChangePasswordModel.newPassword!!)
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.USER_WRITE}')")
    @PutMapping("/{userId}/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun changeProfile(
        @PathVariable userId: Long,
        @RequestBody
        changeProfileInputModel: ChangeProfileInputModel
    ) {
        userService.changeProfile(getSecurityContextHolderUserId(), userId, UserProfile.valueOf(changeProfileInputModel.profile!!.uppercase()))
    }
}
