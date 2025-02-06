package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.LoginModel
import br.com.blifood.api.v1.model.input.LoginInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Login")
interface LoginControllerOpenApi {

    @Operation(summary = "Generate access token with credentials")
    fun login(
        @RequestBody
        loginInputModel: LoginInputModel
    ): LoginModel
}
