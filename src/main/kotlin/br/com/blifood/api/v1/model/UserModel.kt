package br.com.blifood.api.v1.model

import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.Loggable.LogMaskFormat
import br.com.blifood.core.log.Loggable.MaskProperty
import br.com.blifood.domain.entity.User
import io.swagger.v3.oas.annotations.media.Schema

data class UserModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "Carlos Beleli")
    @MaskProperty(LogMaskFormat.NAME)
    val name: String,

    @Schema(example = "beleli@gmail.com")
    @MaskProperty(LogMaskFormat.EMAIL)
    val email: String
) : Loggable

fun User.toModel() = UserModel(id, name, email)
