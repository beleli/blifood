package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.controller.RestaurantProductImageController
import br.com.blifood.domain.entity.ProductImage
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

private const val COLLECTION_RELATION = "images"

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = COLLECTION_RELATION)
open class ProductImageModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "1")
    val restaurantId: Long,

    @Schema(example = "1.jpeg")
    val fileName: String,

    @Schema(example = "Foto do produto")
    val description: String?,

    @Schema(example = "image/jpeg")
    val contentType: String,

    @Schema(example = "202912")
    val size: Long

) : RepresentationModel<ProductImageModel>() {
    companion object {
        fun findByIdLink(restaurantId: Long, id: Long, isSelfRel: Boolean = false) =
            linkTo(methodOn(RestaurantProductImageController::class.java).findById(restaurantId, id))
                .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(findByIdLink(restaurantId, id, true))
    }
}

fun ProductImage.toModel() = ProductImageModel(
    id,
    product.restaurant.id,
    fileName = fileName,
    description = description,
    contentType = contentType,
    size = size
)
