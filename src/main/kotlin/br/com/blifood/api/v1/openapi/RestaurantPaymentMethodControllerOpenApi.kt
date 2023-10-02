package br.com.blifood.api.v1.openapi

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Restaurants")
interface RestaurantPaymentMethodControllerOpenApi {

    @Operation(
        summary = "Add Payment Method to the Restaurant",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        paymentMethodId: Long?
    ): ResponseEntity<Void>

    @Operation(
        summary = "Remove Payment Method to the Restaurant",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun remove(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        paymentMethodId: Long
    ): ResponseEntity<Void>
}
