package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.controller.CityController
import br.com.blifood.domain.entity.City
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

private const val COLLECTION_RELATION = "cities"

@Relation(collectionRelation = COLLECTION_RELATION)
open class CityModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "Uberlândia")
    val name: String,

    @Schema(example = "MG")
    val state: String

) : RepresentationModel<CityModel>() {
    companion object {
        fun findAllLink(isSelfRel: Boolean = false) = linkTo(methodOn(CityController::class.java).findAll())
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(CityController::class.java).findById(id)).withSelfRel())
        this.add(findAllLink())
    }
}

fun City.toModel() = CityModel(id, name, state.name)
