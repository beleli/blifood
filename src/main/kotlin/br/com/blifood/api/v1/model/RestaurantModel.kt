package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.controller.RestaurantController
import br.com.blifood.api.v1.controller.RestaurantPaymentMethodController
import br.com.blifood.core.log.LogMaskFormat
import br.com.blifood.core.log.MaskObject
import br.com.blifood.core.log.MaskProperty
import br.com.blifood.domain.entity.Restaurant
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Pageable
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
    @MaskProperty(LogMaskFormat.NAME)
    val name: String,

    @Schema(example = "10.00")
    val deliveryFee: BigDecimal,

    @Schema(example = "true")
    val active: Boolean,

    @Schema(example = "true")
    val open: Boolean,

    @MaskObject
    val address: AddressModel,

    val culinary: CulinaryModel,

    val paymentMethods: List<PaymentMethodModel>

) : RepresentationModel<RestaurantModel>() {
    companion object {
        fun findAllLink(pageable: Pageable, isSelfRel: Boolean = false) = linkTo(methodOn(RestaurantController::class.java).findAll(pageable))
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

        this.add(
            linkTo(methodOn(RestaurantPaymentMethodController::class.java).add(id, null))
                .withRel("paymentMethods")
        )

        this.add(ProductModel.findAllLink(id, Pageable.ofSize(DEFAULT_PAGE_SIZE)))
        this.add(findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE)))
    }
}

fun Restaurant.toModel() =
    RestaurantModel(
        id = id,
        name = name,
        deliveryFee = deliveryFee,
        active = isActive(),
        open = isOpen(),
        address = address.toModel(),
        culinary = culinary.toModel(),
        paymentMethods = paymentsMethods.map { it.toModel() }
    )
