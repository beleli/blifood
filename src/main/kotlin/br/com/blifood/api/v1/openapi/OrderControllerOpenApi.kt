package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.OrderModel
import br.com.blifood.api.v1.model.input.OrderInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Orders")
interface OrderControllerOpenApi {

    @Operation(summary = "Find Culinary by Code")
    fun findByCode(
        @PathVariable
        orderCode: String
    ): OrderModel

    @Operation(summary = "Insert a Order")
    fun create(
        @RequestBody
        orderInputModel: OrderInputModel
    ): OrderModel

    @Operation(summary = "Confirm Order by Code")
    fun confirm(
        @PathVariable
        orderCode: String
    ): ResponseEntity<Unit>

    @Operation(summary = "Delivery Order by Code")
    fun delivery(
        @PathVariable
        orderCode: String
    ): ResponseEntity<Unit>

    @Operation(summary = "Cancel Order by Code")
    fun cancel(
        @PathVariable
        orderCode: String
    ): ResponseEntity<Unit>
}
