package br.com.blifood.api.v1.model.input

import br.com.blifood.domain.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserWithPasswordInputModel(

    @Schema(example = "Carlos Beleli")
    @field:NotBlank
    @field:Size(max = 80)
    val name: String? = null,

    @Schema(example = "beleli@gmail.com")
    @field:NotBlank
    @field:Email
    val email: String? = null,

    @Schema(example = "test123")
    @field:NotBlank
    val password: String? = null
)
fun UserWithPasswordInputModel.toEntity() = User(
    name = name!!,
    email = email!!,
    password = password!!
)
