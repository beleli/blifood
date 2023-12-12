package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.LogMaskFormat
import br.com.blifood.core.log.MaskProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginInputModel(

    @Schema(example = "gerente@blifood.com.br")
    @MaskProperty(LogMaskFormat.EMAIL)
    @field:NotBlank
    @field:Email
    val username: String? = null,

    @Schema(example = "test123")
    @MaskProperty
    @field:NotBlank
    val password: String? = null
)
