package br.com.blifood.api.v1.model.input

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginInputModel(

    @Schema(example = "gerente@blifood.com.br")
    @field:NotBlank
    @field:Email
    val username: String? = null,

    @Schema(example = "test123")
    @field:NotBlank
    val password: String? = null
)
