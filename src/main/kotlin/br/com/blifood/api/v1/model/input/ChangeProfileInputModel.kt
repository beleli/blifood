package br.com.blifood.api.v1.model.input

import br.com.blifood.api.validation.EnumValue
import br.com.blifood.core.log.Loggable
import br.com.blifood.domain.entity.UserProfile
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class ChangeProfileInputModel(

    @Schema(example = "MANAGER")
    @field:NotBlank
    @field:EnumValue(enumClass = UserProfile::class)
    val profile: String?
) : Loggable
