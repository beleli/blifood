package br.com.blifood.api.v1.model.input

import br.com.blifood.domain.entity.OrderItem
import br.com.blifood.domain.entity.Product
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class OrderItemInputModel(

    @Schema(example = "1")
    @field:NotNull
    @field:Positive
    val productId: Long?,

    @Schema(example = "1")
    @field:NotNull
    @field:Positive
    val amount: Int?,

    @Schema(example = "Sem pimenta")
    val observation: String?
)
fun OrderItemInputModel.toEntity() = OrderItem(
    product = Product(id = productId!!),
    amount = amount!!,
    observation = observation
)
