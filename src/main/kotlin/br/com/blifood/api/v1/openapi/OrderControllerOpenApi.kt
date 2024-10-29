package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.OrderModel
import br.com.blifood.api.v1.model.input.OrderInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Orders")
interface OrderControllerOpenApi {

    @Operation(
        summary = "Find Culinary by Code",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findByCode(
        @PathVariable
        orderCode: String
    ): OrderModel

    @Operation(
        summary = "Insert a Order",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody
        orderInputModel: OrderInputModel
    ): OrderModel

    @Operation(
        summary = "Confirm Order by Code",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun confirm(
        @PathVariable
        orderCode: String
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Delivery Order by Code",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun delivery(
        @PathVariable
        orderCode: String
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Cancel Order by Code",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun cancel(
        @PathVariable
        orderCode: String
    ): ResponseEntity<Unit>
}
