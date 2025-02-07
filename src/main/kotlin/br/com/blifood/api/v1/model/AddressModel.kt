package br.com.blifood.api.v1.model

import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.Loggable.LogMaskFormat
import br.com.blifood.core.log.Loggable.MaskProperty
import br.com.blifood.domain.entity.Address
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
class AddressModel(

    @Schema(example = "Uberlândia")
    @MaskProperty
    val city: String,

    @Schema(example = "38400000")
    @MaskProperty
    val zipCode: String,

    @Schema(example = "Rua Floriano Peixoto")
    @MaskProperty(LogMaskFormat.ADDRESS)
    val street: String,

    @Schema(example = "1500")
    @MaskProperty
    val number: String,

    @Schema(example = "Apto 901")
    @MaskProperty
    val complement: String?,

    @Schema(example = "Centro")
    @MaskProperty
    val district: String
) : Loggable

fun Address.toModel() = AddressModel(
    city.name,
    zipCode,
    street,
    number,
    complement,
    district
)
