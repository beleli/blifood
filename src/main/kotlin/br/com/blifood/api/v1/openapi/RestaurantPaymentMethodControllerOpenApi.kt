package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.PaymentMethodModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.CollectionModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "Restaurants")
interface RestaurantPaymentMethodControllerOpenApi {

    @Operation(summary = "List all Payment Methods of a restaurant")
    fun findAll(
        @PathVariable restaurantId: Long
    ): CollectionModel<PaymentMethodModel>

    @Operation(summary = "Add Payment Method to the Restaurant")
    fun add(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        paymentMethodId: Long?
    ): ResponseEntity<Unit>

    @Operation(summary = "Remove Payment Method to the Restaurant")
    fun remove(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        paymentMethodId: Long
    ): ResponseEntity<Unit>
}
