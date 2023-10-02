package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.ProductImageModel
import br.com.blifood.api.v1.model.input.ProductImageInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Products")
interface RestaurantProductImageControllerOpenApi {

    @Operation(
        summary = "Find Product Image by Product Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    ): ProductImageModel

    /*@Operation(
        summary = "Get Product Image by Product Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun download(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    ): Any*/

    @Operation(
        summary = "Upload Product Image by Product Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun upload(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long,
        productImageInput: ProductImageInputModel
    ): ProductImageModel

    @Operation(
        summary = "Remove Product Image by Product Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun remove(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    )
}
