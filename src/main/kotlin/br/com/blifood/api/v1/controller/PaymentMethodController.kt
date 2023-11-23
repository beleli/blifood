package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.input.PaymentMethodInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.PaymentMethodControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.service.PaymentMethodService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
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

    @PreAuthorize("hasAuthority('${Authority.PAYMENT_METHOD_READ}')")
    @GetMapping
    override fun findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable): PagedModel<PaymentMethodModel> {
        val page = paymentMethodService.findAll(pageable).map { it.toModel() }
        return PagedModel.of(
            page.content,
            PagedModel.PageMetadata(page.size.toLong(), page.number.toLong(), page.totalElements),
            PaymentMethodModel.findAllLink(pageable, true)
        )
    }

    @PreAuthorize("hasAuthority('${Authority.PAYMENT_METHOD_READ}')")
    @GetMapping("/{paymentMethodId}")
    override fun findById(@PathVariable paymentMethodId: Long): PaymentMethodModel {
        return paymentMethodService.findOrThrow(paymentMethodId).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.PAYMENT_METHOD_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        paymentMethodInputModel: PaymentMethodInputModel
    ): PaymentMethodModel {
        return paymentMethodService.save(paymentMethodInputModel.toEntity()).toModel().also {
            addUriInResponseHeader(it.id)
        }
    }

    @PreAuthorize("hasAuthority('${Authority.PAYMENT_METHOD_WRITE}')")
    @PutMapping("/{paymentMethodId}")
    override fun alter(
        @PathVariable paymentMethodId: Long,
        @Valid @RequestBody
        paymentMethodInputModel: PaymentMethodInputModel
    ): PaymentMethodModel {
        val paymentMethod = paymentMethodService.findOrThrow(paymentMethodId).applyModel(paymentMethodInputModel)
        return paymentMethodService.save(paymentMethod).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.PAYMENT_METHOD_WRITE}')")
    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable paymentMethodId: Long) {
        paymentMethodService.delete(paymentMethodId)
    }
}
