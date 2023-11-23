package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.ProductModel
import br.com.blifood.api.v1.model.input.ProductInputModel
import br.com.blifood.core.openapi.PageableParameter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Products")
interface RestaurantProductControllerOpenApi {

    @PageableParameter
    @Operation(
        summary = "List all products of a restaurant",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(hidden = true) pageable: Pageable
    ): PagedModel<ProductModel>

    @Operation(
        summary = "Find Product by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    ): ProductModel

    @Operation(
        summary = "Insert a Product",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @RequestBody(required = true)
        productInputModel: ProductInputModel
    ): ProductModel

    @Operation(
        summary = "Update Product by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    @PutMapping("/{productId}")
    fun alter(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long,
        @RequestBody(required = true)
        productInputModel: ProductInputModel
    ): ProductModel

    @Operation(
        summary = "Delete Product by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun delete(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    )
}
