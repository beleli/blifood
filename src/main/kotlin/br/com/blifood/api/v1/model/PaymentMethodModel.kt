package br.com.blifood.api.v1.model

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.controller.PaymentMethodController
import br.com.blifood.domain.entity.PaymentMethod
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

private const val COLLECTION_RELATION = "paymentMethods"

@Relation(collectionRelation = COLLECTION_RELATION)
open class PaymentMethodModel(

    @Schema(example = "1")
    val id: Long,

    @Schema(example = "Cartão de Credito")
    val description: String

) : RepresentationModel<PaymentMethodModel>() {
    companion object {
        fun findAllLink(pageable: Pageable, isSelfRel: Boolean = false) = linkTo(methodOn(PaymentMethodController::class.java).findAll(pageable))
            .withRel(if (isSelfRel) IanaLinkRelations.SELF_VALUE else COLLECTION_RELATION)
    }
    init {
        this.add(linkTo(methodOn(PaymentMethodController::class.java).findById(id)).withSelfRel())
        this.add(findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE)))
    }
}

fun PaymentMethod.toModel() = PaymentMethodModel(id, description)
