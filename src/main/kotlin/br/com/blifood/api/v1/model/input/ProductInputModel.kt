package br.com.blifood.api.v1.model.input

import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.entity.Restaurant
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ProductInputModel(

    @Schema(example = "Marmita")
    @field:NotBlank
    @field:Size(max = 80)
    val name: String?,

    @Schema(example = "Marmita com 2 carnes")
    @field:NotBlank
    val description: String?,

    @Schema(example = "10.00")
    @field:NotNull
    @field:PositiveOrZero
    val price: BigDecimal?
)

fun ProductInputModel.toEntity(restaurantId: Long) =
    Product(
        restaurant = Restaurant(restaurantId),
        name = name!!,
        description = description!!,
        price = price!!
    )

fun Product.applyModel(productInputModel: ProductInputModel) =
    this.apply {
        name = productInputModel.name!!
        description = productInputModel.description!!
        price = productInputModel.price!!
    }
