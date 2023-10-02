package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.controller.RestaurantProductController
import br.com.blifood.domain.entity.Product
import io.swagger.v3.oas.annotations.media.Schema
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
    val name: String,

    @Schema(example = "Marmita com 2 carnes")
    val description: String,

    @Schema(example = "10.00")
    val price: BigDecimal,

    val active: Boolean

) : RepresentationModel<StateModel>() {
    companion object {
        fun findAllLink(restaurantId: Long, isSelfRel: Boolean = false) = linkTo(methodOn(RestaurantProductController::class.java).findAll(restaurantId))
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(RestaurantProductController::class.java).findById(restaurantId, id)).withSelfRel())
        this.add(ProductImageModel.findByIdLink(restaurantId, id))
        this.add(findAllLink(restaurantId))
    }
}

fun Product.toModel() =
    ProductModel(
        id,
        restaurant.id,
        name,
        description,
        price,
        active
    )
