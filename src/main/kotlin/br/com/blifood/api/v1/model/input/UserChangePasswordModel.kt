package br.com.blifood.api.v1.model.input

import jakarta.validation.constraints.NotBlank

data class UserChangePasswordModel(
    @field:NotBlank
    val password: String? = null,

    @field:NotBlank
    val newPassword: String? = null
)
