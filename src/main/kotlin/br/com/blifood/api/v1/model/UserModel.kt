package br.com.blifood.api.v1.model

import br.com.blifood.domain.entity.User
import io.swagger.v3.oas.annotations.media.Schema

data class UserModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "Carlos Beleli")
    val name: String,

    @Schema(example = "beleli@gmail.com")
    val email: String
)
fun User.toModel() = UserModel(id, name, email)
