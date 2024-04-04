package br.com.blifood.api.v1.controller

import br.com.blifood.api.aspect.LogAndValidate
import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.getRequestContextHolderUserId
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.RestaurantPaymentMethodControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.RestaurantService
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/restaurants/{restaurantId}/payment-methods", produces = [MediaType.APPLICATION_JSON_VALUE])
class RestaurantPaymentMethodController(
    private val restaurantService: RestaurantService
) : RestaurantPaymentMethodControllerOpenApi {

    @LogAndValidate(validateRequest = false, logResponse = false)
    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @GetMapping
    override fun findAll(
        @PathVariable restaurantId: Long
    ): CollectionModel<PaymentMethodModel> {
        try {
            return CollectionModel.of(
                restaurantService.findAllPaymentMethods(restaurantId).map { it.toModel() },
                PaymentMethodModel.findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE), true)
            )
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun add(
        @PathVariable restaurantId: Long,
        @PathVariable paymentMethodId: Long?
    ): ResponseEntity<Void> {
        try {
            restaurantService.addPaymentMethod(restaurantId, getRequestContextHolderUserId(), paymentMethodId!!)
            return ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun remove(
        @PathVariable restaurantId: Long,
        @PathVariable paymentMethodId: Long
    ): ResponseEntity<Void> {
        try {
            restaurantService.removePaymentMethod(restaurantId, getRequestContextHolderUserId(), paymentMethodId)
            return ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }
}
