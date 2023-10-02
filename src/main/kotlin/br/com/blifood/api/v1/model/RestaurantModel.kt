package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.controller.RestaurantController
import br.com.blifood.api.v1.controller.RestaurantPaymentMethodController
import br.com.blifood.domain.entity.Restaurant
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import java.math.BigDecimal

private const val COLLECTION_RELATION = "restaurants"

@Relation(collectionRelation = COLLECTION_RELATION)
open class RestaurantModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "Celina")
    val name: String,

    @Schema(example = "10.00")
    val deliveryFee: BigDecimal,

    @Schema(example = "true")
    val active: Boolean,

    @Schema(example = "true")
    val open: Boolean,

    val address: AddressModel,

    val culinary: CulinaryModel,

    val paymentMethods: List<PaymentMethodModel>

) : RepresentationModel<RestaurantModel>() {
    companion object {
        fun findAllLink(isSelfRel: Boolean = false) = linkTo(methodOn(RestaurantController::class.java).findAll())
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(RestaurantController::class.java).findById(id)).withSelfRel())

        if (!open) {
            this.add(linkTo(methodOn(RestaurantController::class.java).open(id)).withRel("open"))
        } else {
            this.add(linkTo(methodOn(RestaurantController::class.java).close(id)).withRel("close"))
        }

        if (!active) {
            this.add(linkTo(methodOn(RestaurantController::class.java).activate(id)).withRel("activate"))
        } else {
            this.add(linkTo(methodOn(RestaurantController::class.java).inactivate(id)).withRel("inactivate"))
        }

        this.add(linkTo(methodOn(RestaurantPaymentMethodController::class.java).create(id, null))
            .withRel("paymentMethods"))

        this.add(ProductModel.findAllLink(id))
        this.add(findAllLink())
    }
}

fun Restaurant.toModel() =
    RestaurantModel(
        id,
        name,
        deliveryFee,
        isActive(),
        isOpen(),
        address.toModel(),
        culinary.toModel(),
        paymentsMethods.map { it.toModel() }
    )
