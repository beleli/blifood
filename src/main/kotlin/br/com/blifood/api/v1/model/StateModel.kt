package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.controller.StateController
import br.com.blifood.domain.entity.State
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

private const val COLLECTION_RELATION = "states"

@Relation(collectionRelation = COLLECTION_RELATION)
open class StateModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "MG")
    val name: String

) : RepresentationModel<StateModel>() {
    companion object {
        fun findAllLink(pageable: Pageable, isSelfRel: Boolean = false) = linkTo(methodOn(StateController::class.java).findAll(pageable))
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(StateController::class.java).findById(id)).withSelfRel())
        this.add(findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE)))
    }
}

fun State.toModel() = StateModel(id, name)
