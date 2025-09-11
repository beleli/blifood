package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.Loggable.LogMaskFormat
import br.com.blifood.core.log.Loggable.MaskProperty
import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.entity.Restaurant
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class RestaurantInputModel(

    @Schema(example = "Celina")
    @MaskProperty(LogMaskFormat.NAME)
    @field:NotBlank(message = "constraints.not-blank")
    @field:Size(max = 80, message = "constraints.size")
    val name: String?,

    @Schema(example = "10.00")
    @field:NotNull(message = "constraints.not-null")
    @field:PositiveOrZero(message = "constraints.positive-or-zero")
    val deliveryFee: BigDecimal?,

    @Schema(example = "1")
    @field:NotNull(message = "constraints.not-null")
    @field:Positive(message = "constraints.positive")
    val culinaryId: Long?,

    @field:Valid
    @field:NotNull(message = "constraints.not-null")
    val address: AddressInputModel?
) : Loggable

fun RestaurantInputModel.toEntity() =
    Restaurant(
        culinary = Culinary(id = culinaryId!!),
        address = address!!.toEntity(),
        name = name!!,
        deliveryFee = deliveryFee!!
    )

fun Restaurant.applyModel(restaurantInputModel: RestaurantInputModel) =
    this.apply {
        culinary = Culinary(id = restaurantInputModel.culinaryId!!)
        address = restaurantInputModel.address!!.toEntity()
        name = restaurantInputModel.name!!
        deliveryFee = restaurantInputModel.deliveryFee!!
    }
