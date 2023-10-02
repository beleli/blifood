package br.com.blifood.api.v1.model

import br.com.blifood.domain.entity.OrderItem
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
class OrderItemModel(

    @Schema(example = "1")
    val productId: Long,

    @Schema(example = "Marmita")
    val productName: String,

    @Schema(example = "2")
    val amount: Int,

    @Schema(example = "10.00")
    val unitPrice: BigDecimal,

    @Schema(example = "20.00")
    val total: BigDecimal,

    @Schema(example = "Sem pimenta")
    val observation: String?
)
fun OrderItem.toModel() = OrderItemModel(
    product.id,
    product.name,
    amount,
    unitPrice,
    total(),
    observation
)
