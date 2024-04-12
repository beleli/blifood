package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.controller.OrderController
import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.entity.OrderStatus
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import java.math.BigDecimal
import java.time.OffsetDateTime

private const val COLLECTION_RELATION = "orders"

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = COLLECTION_RELATION)
open class OrderModel(

    @Schema(example = "ddeb8e8e-6462-492f-8124-80fa0dcd261a")
    val code: String,

    @Schema(example = "20.00")
    val subtotal: BigDecimal,

    @Schema(example = "2.00")
    val deliveryFee: BigDecimal,

    @Schema(example = "22.00")
    val total: BigDecimal,

    @Schema(example = "DELIVERED")
    val status: OrderStatus,

    @Schema(example = "2023-08-16T17:59:23.122338Z")
    val created: OffsetDateTime,

    @Schema(example = "2023-08-16T17:59:23.122338Z")
    val confirmed: OffsetDateTime?,

    @Schema(example = "2023-08-16T17:59:23.122338Z")
    val canceled: OffsetDateTime?,

    @Schema(example = "2023-08-16T17:59:23.122338Z")
    val delivered: OffsetDateTime?,

    val restaurant: RestaurantModel,

    val paymentMethod: PaymentMethodModel,

    val deliveryAddress: AddressModel,

    val items: List<OrderItemModel>
) : RepresentationModel<OrderModel>() {
    companion object {
        /*fun controllerLink(isSelfRel: Boolean = false) = linkTo(OrderController::class.java)
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)*/
        /*fun findAllLink(isSelfRel: Boolean = false) = linkTo(methodOn(OrderController::class.java).findAll())
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)*/
    }
    init {
        this.add(linkTo(methodOn(OrderController::class.java).findByCode(code)).withSelfRel())
        if (status.isChangeableTo(OrderStatus.CONFIRMED)) {
            this.add(linkTo(methodOn(OrderController::class.java).confirm(code)).withRel("confirm"))
        }
        if (status.isChangeableTo(OrderStatus.DELIVERED)) {
            this.add(linkTo(methodOn(OrderController::class.java).delivery(code)).withRel("delivery"))
        }
        if (status.isChangeableTo(OrderStatus.CANCELED)) {
            this.add(linkTo(methodOn(OrderController::class.java).cancel(code)).withRel("cancel"))
        }
    }
}

fun Order.toModel() = OrderModel(
    code,
    subtotal,
    deliveryFee,
    total(),
    status(),
    created(),
    confirmed(),
    canceled(),
    delivered(),
    restaurant.toModel(),
    paymentMethod.toModel(),
    deliveryAddress.toModel(),
    items.map { it.toModel() }
)
