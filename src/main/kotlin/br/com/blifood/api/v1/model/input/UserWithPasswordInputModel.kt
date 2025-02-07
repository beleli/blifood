package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.LogMaskFormat
import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.MaskProperty
import br.com.blifood.domain.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserWithPasswordInputModel(

    @Schema(example = "Carlos Beleli")
    @MaskProperty(LogMaskFormat.NAME)
    @field:NotBlank
    @field:Size(max = 80)
    val name: String? = null,

    @Schema(example = "beleli@gmail.com")
    @MaskProperty(LogMaskFormat.EMAIL)
    @field:NotBlank
    @field:Email
    val email: String? = null,

    @Schema(example = "test123")
    @MaskProperty
    @field:NotBlank
    val password: String? = null
) : Loggable

fun UserWithPasswordInputModel.toEntity() = User(
    name = name!!,
    email = email!!,
    password = password!!
)
