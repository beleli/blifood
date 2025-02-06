package br.com.blifood.api.v1.openapi

import br.com.blifood.api.openapi.PageableParameter
import br.com.blifood.api.v1.model.ProductModel
import br.com.blifood.api.v1.model.input.ProductInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Products")
interface RestaurantProductControllerOpenApi {

    @PageableParameter
    @Operation(summary = "List all products of a restaurant")
    fun findAll(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(hidden = true) pageable: Pageable
    ): PagedModel<ProductModel>

    @Operation(summary = "Find Product by Id")
    fun findById(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    ): ProductModel

    @Operation(summary = "Insert a Product")
    fun create(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @RequestBody(required = true)
        productInputModel: ProductInputModel
    ): ProductModel

    @Operation(summary = "Update Product by Id")
    @PutMapping("/{productId}")
    fun alter(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long,
        @RequestBody(required = true)
        productInputModel: ProductInputModel
    ): ProductModel

    @Operation(summary = "Delete Product by Id")
    fun delete(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        productId: Long
    )
}
