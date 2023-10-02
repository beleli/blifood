package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.UserModel
import br.com.blifood.api.v1.model.input.UserChangePasswordModel
import br.com.blifood.api.v1.model.input.UserInputModel
import br.com.blifood.api.v1.model.input.UserWithPasswordInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Users")
interface UserControllerOpenApi {

    @Operation(
        summary = "Find User by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        userId: Long
    ): UserModel

    @Operation(
        summary = "Insert a User",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody(required = true)
        userWithPasswordInputDto: UserWithPasswordInputModel
    ): UserModel

    @Operation(
        summary = "Update User by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun alter(
        @Parameter(example = "1", required = true)
        userId: Long,
        @RequestBody(required = true)
        userInputDto: UserInputModel
    ): UserModel

    @Operation(
        summary = "Update user password by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun changePassword(
        @Parameter(example = "1", required = true)
        userId: Long,
        @RequestBody(required = true)
        userChangePasswordDto: UserChangePasswordModel
    )
}
