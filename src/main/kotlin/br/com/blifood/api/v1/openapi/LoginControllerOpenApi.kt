package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.LoginModel
import br.com.blifood.api.v1.model.input.LoginInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Login")
interface LoginControllerOpenApi {

    @Operation(
        summary = "Generate access token with credentials",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun login(
        @RequestBody
        loginInputModel: LoginInputModel
    ): LoginModel
}
