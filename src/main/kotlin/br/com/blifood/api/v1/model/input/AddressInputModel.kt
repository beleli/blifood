package br.com.blifood.api.v1.model.input

import br.com.blifood.api.validation.NumericValue
import br.com.blifood.core.log.LogMaskFormat
import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.MaskProperty
import br.com.blifood.domain.entity.Address
import br.com.blifood.domain.entity.City
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class AddressInputModel(

    @Schema(example = "1")
    @field:NotNull
    @field:Positive
    val cityId: Long?,

    @Schema(example = "38400000")
    @MaskProperty
    @field:NumericValue
    @field:NotBlank
    @field:Size(min = 8, max = 8)
    val zipCode: String?,

    @Schema(example = "Rua Floriano Peixoto")
    @MaskProperty(LogMaskFormat.ADDRESS)
    @field:NotBlank
    @field:Size(max = 100)
    val street: String?,

    @Schema(example = "1500")
    @MaskProperty
    @field:NotBlank
    @field:Size(max = 20)
    val number: String?,

    @Schema(example = "Apto 901")
    @MaskProperty
    @field:Size(max = 60)
    val complement: String?,

    @Schema(example = "Centro")
    @MaskProperty
    @field:NotBlank
    @field:Size(max = 60)
    val district: String?
) : Loggable

fun AddressInputModel.toEntity() = Address(
    City(id = this.cityId!!),
    zipCode!!,
    street!!,
    number!!,
    complement,
    district!!
)
