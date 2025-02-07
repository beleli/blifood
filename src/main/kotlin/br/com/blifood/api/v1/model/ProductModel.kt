package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.controller.RestaurantProductController
import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.Loggable.MaskProperty
import br.com.blifood.domain.entity.Product
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import java.math.BigDecimal

private const val COLLECTION_RELATION = "products"

@Relation(collectionRelation = COLLECTION_RELATION)
open class ProductModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "1")
    val restaurantId: Long,

    @Schema(example = "Marmita")
    @MaskProperty
    val name: String,

    @Schema(example = "Marmita com 2 carnes")
    @MaskProperty
    val description: String,

    @Schema(example = "10.00")
    val price: BigDecimal,

    val active: Boolean

) : RepresentationModel<StateModel>(), Loggable {
    companion object {
        fun findAllLink(restaurantId: Long, pageable: Pageable, isSelfRel: Boolean = false) = linkTo(methodOn(RestaurantProductController::class.java).findAll(restaurantId, pageable))
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(RestaurantProductController::class.java).findById(restaurantId, id)).withSelfRel())
        this.add(ProductImageModel.findByIdLink(restaurantId, id))
        this.add(findAllLink(restaurantId, Pageable.ofSize(DEFAULT_PAGE_SIZE)))
    }
}

fun Product.toModel() =
    ProductModel(
        id = id,
        restaurantId = restaurant.id,
        name = name,
        description = description,
        price = price,
        active = active
    )
