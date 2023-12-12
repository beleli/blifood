package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.LogMaskFormat
import br.com.blifood.core.log.MaskObject
import br.com.blifood.core.log.MaskProperty
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
    @field:NotBlank
    @field:Size(max = 80)
    val name: String?,

    @Schema(example = "10.00")
    @field:NotNull
    @field:PositiveOrZero
    val deliveryFee: BigDecimal?,

    @Schema(example = "1")
    @field:NotNull
    @field:Positive
    val culinaryId: Long?,

    @MaskObject
    @field:NotNull
    @field:Valid
    val address: AddressInputModel?
)
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
