package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.UserModel
import br.com.blifood.api.v1.model.input.ChangeProfileInputModel
import br.com.blifood.api.v1.model.input.UserChangePasswordModel
import br.com.blifood.api.v1.model.input.UserInputModel
import br.com.blifood.api.v1.model.input.UserWithPasswordInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Users")
interface UserControllerOpenApi {

    @Operation(summary = "Find User by Id")
    fun findById(
        @Parameter(example = "1", required = true)
        userId: Long
    ): UserModel

    @Operation(summary = "Insert a User")
    fun create(
        @RequestBody(required = true)
        userWithPasswordInputModel: UserWithPasswordInputModel
    ): UserModel

    @Operation(summary = "Update User by Id")
    fun alter(
        @Parameter(example = "1", required = true)
        userId: Long,
        @RequestBody(required = true)
        userInputModel: UserInputModel
    ): UserModel

    @Operation(summary = "Update user password by Id")
    fun changePassword(
        @Parameter(example = "1", required = true)
        userId: Long,
        @RequestBody(required = true)
        userChangePasswordModel: UserChangePasswordModel
    )

    @Operation(summary = "Update user profile by Id")
    fun changeProfile(
        @Parameter(example = "1", required = true)
        userId: Long,
        @RequestBody
        changeProfileInputModel: ChangeProfileInputModel
    )
}
