package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class OrderInputModel(

    @Schema(example = "1")
    @field:NotNull
    @field:Positive
    val restaurantId: Long?,

    @Schema(example = "1")
    @field:NotNull
    @field:Positive
    val paymentMethodId: Long?,

    @field:NotNull
    @field:Valid
    val deliveryAddress: AddressInputModel?,

    @field:NotNull
    @field:Size(min = 1)
    val items: List<OrderItemInputModel>?
) : Loggable

fun OrderInputModel.toEntity(userId: Long) = Order(
    user = User(id = userId),
    restaurant = Restaurant(id = restaurantId!!),
    paymentMethod = PaymentMethod(id = paymentMethodId!!),
    deliveryAddress = deliveryAddress!!.toEntity(),
    items = items!!.map { it.toEntity() }
)
