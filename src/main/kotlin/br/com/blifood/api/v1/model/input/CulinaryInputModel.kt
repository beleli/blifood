package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.domain.entity.Culinary
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CulinaryInputModel(

    @Schema(example = "1")
    @field:NotBlank
    @field:Size(max = 60)
    val name: String?
) : Loggable

fun CulinaryInputModel.toEntity() = Culinary(name = name!!)

fun Culinary.applyModel(culinaryInputModel: CulinaryInputModel) = this.apply { name = culinaryInputModel.name!! }
