package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.MaskProperty
import jakarta.validation.constraints.NotBlank

data class UserChangePasswordModel(

    @MaskProperty
    @field:NotBlank
    val password: String? = null,

    @MaskProperty
    @field:NotBlank
    val newPassword: String? = null
) : Loggable
