package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.State
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CityInputModel(

    @Schema(example = "Uberlândia")
    @field:NotBlank(message = "constraints.not-blank")
    @field:Size(max = 80, message = "constraints.size")
    val name: String?,

    @Schema(example = "1")
    @field:NotNull(message = "constraints.not-null")
    @field:Positive(message = "constraints.positive")
    val stateId: Long?
) : Loggable

fun CityInputModel.toEntity() = City(name = name!!, state = State(id = stateId!!))

fun City.applyModel(cityInputModel: CityInputModel) = this.apply {
    name = cityInputModel.name!!
    state = State(id = cityInputModel.stateId!!)
}
