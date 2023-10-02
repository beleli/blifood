package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.ResourceUriHelper
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.input.PaymentMethodInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.PaymentMethodControllerOpenApi
import br.com.blifood.domain.service.PaymentMethodService
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/payment-methods", produces = [MediaType.APPLICATION_JSON_VALUE])
class PaymentMethodController(
    private val paymentMethodService: PaymentMethodService
) : PaymentMethodControllerOpenApi {

    @GetMapping
    override fun findAll(): CollectionModel<PaymentMethodModel> {
        return CollectionModel.of(
            paymentMethodService.findAll().map { it.toModel() },
            PaymentMethodModel.findAllLink(true)
        )
    }

    @GetMapping("/{paymentMethodId}")
    override fun findById(@PathVariable paymentMethodId: Long): PaymentMethodModel {
        return paymentMethodService.findOrThrow(paymentMethodId).toModel()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        paymentMethodInputDto: PaymentMethodInputModel
    ): PaymentMethodModel {
        return paymentMethodService.save(paymentMethodInputDto.toEntity()).toModel().also {
            ResourceUriHelper.addUriInResponseHeader(it.id)
        }
    }

    @PutMapping("/{paymentMethodId}")
    override fun alter(
        @PathVariable paymentMethodId: Long,
        @Valid @RequestBody
        paymentMethodInputDto: PaymentMethodInputModel
    ): PaymentMethodModel {
        val paymentMethod = paymentMethodService.findOrThrow(paymentMethodId).applyModel(paymentMethodInputDto)
        return paymentMethodService.save(paymentMethod).toModel()
    }

    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable paymentMethodId: Long) {
        paymentMethodService.delete(paymentMethodId)
    }
}
