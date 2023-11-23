package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.input.PaymentMethodInputModel
import br.com.blifood.core.openapi.PageableParameter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "PaymentMethods")
interface PaymentMethodControllerOpenApi {

    @PageableParameter
    @Operation(
        summary = "List all Payment Methods",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(@Parameter(hidden = true) pageable: Pageable): PagedModel<PaymentMethodModel>

    @Operation(
        summary = "Find Payment Method by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        paymentMethodId: Long
    ): PaymentMethodModel

    @Operation(
        summary = "Insert a Payment Method",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody(required = true)
        paymentMethodInputModel: PaymentMethodInputModel
    ): PaymentMethodModel

    @Operation(
        summary = "Update Payment Method by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun alter(
        @Parameter(example = "1", required = true)
        paymentMethodId: Long,
        @RequestBody(required = true)
        paymentMethodInputModel: PaymentMethodInputModel
    ): PaymentMethodModel

    @Operation(
        summary = "Delete Payment Method by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun delete(
        @Parameter(example = "1", required = true)
        paymentMethodId: Long
    )
}
