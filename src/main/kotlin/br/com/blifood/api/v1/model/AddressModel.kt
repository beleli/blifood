package br.com.blifood.api.v1.model

import br.com.blifood.domain.entity.Address
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
class AddressModel(

    @Schema(example = "Uberlândia")
    val city: String,

    @Schema(example = "38400-000")
    val zipCode: String,

    @Schema(example = "Rua Floriano Peixoto")
    val street: String,

    @Schema(example = "1500")
    val number: String,

    @Schema(example = "Apto 901")
    val complement: String?,

    @Schema(example = "Centro")
    val district: String
)

fun Address.toModel() = AddressModel(
    city.name,
    zipCode,
    street,
    number,
    complement,
    district
)
