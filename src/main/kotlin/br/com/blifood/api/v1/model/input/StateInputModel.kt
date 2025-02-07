package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.domain.entity.State
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class StateInputModel(

    @Schema(example = "MG")
    @field:NotBlank
    @field:Size(min = 2, max = 2)
    val name: String?
) : Loggable

fun StateInputModel.toEntity() = State(name = name!!)

fun State.applyModel(stateInputModel: StateInputModel) = this.apply { name = stateInputModel.name!! }
