package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.controller.CulinaryController
import br.com.blifood.domain.entity.Culinary
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

private const val COLLECTION_RELATION = "culinary"

@Relation(collectionRelation = COLLECTION_RELATION)
open class CulinaryModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "Brasileira")
    val name: String

) : RepresentationModel<CulinaryModel>() {
    companion object {
        fun findAllLink(isSelfRel: Boolean = false) = linkTo(methodOn(CulinaryController::class.java).findAll())
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(CulinaryController::class.java).findById(id)).withSelfRel())
        this.add(findAllLink())
    }
}

fun Culinary.toModel() = CulinaryModel(id, name)
