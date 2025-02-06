package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.ProductImageModel
import br.com.blifood.api.v1.model.input.ProductImageInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Products")
interface RestaurantProductImageControllerOpenApi {

    @Operation(summary = "Find Product Image by Product Id")
    fun findById(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    ): ProductImageModel

    /*@Operation(summary = "Get Product Image by Product Id")
    fun download(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    ): Any*/

    @Operation(summary = "Upload Product Image by Product Id")
    fun upload(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long,
        productImageInput: ProductImageInputModel
    ): ProductImageModel

    @Operation(summary = "Remove Product Image by Product Id")
    fun remove(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    )
}
