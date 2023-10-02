package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.openapi.RestaurantPaymentMethodControllerOpenApi
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.RestaurantService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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

    @PutMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun create(
        @PathVariable restaurantId: Long,
        @PathVariable paymentMethodId: Long?
    ) : ResponseEntity<Void> {
        try {
            restaurantService.addPaymentMethod(restaurantId, paymentMethodId!!)
            return ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }

    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun remove(
        @PathVariable restaurantId: Long,
        @PathVariable paymentMethodId: Long
    ) : ResponseEntity<Void> {
        try {
            restaurantService.removePaymentMethod(restaurantId, paymentMethodId)
            return ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }
}
