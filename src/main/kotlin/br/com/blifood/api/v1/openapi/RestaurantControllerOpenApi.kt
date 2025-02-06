package br.com.blifood.api.v1.openapi

import br.com.blifood.api.openapi.PageableParameter
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.input.RestaurantInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Restaurants")
interface RestaurantControllerOpenApi {

    @PageableParameter
    @Operation(summary = "List all Restaurants")
    fun findAll(@Parameter(hidden = true) pageable: Pageable): PagedModel<RestaurantModel>

    @Operation(summary = "Find Restaurant by Id")
    fun findById(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): RestaurantModel

    @Operation(summary = "Insert a Restaurant")
    fun create(
        @RequestBody(required = true)
        restaurantInputModel: RestaurantInputModel
    ): RestaurantModel

    @Operation(summary = "Update Restaurant by Id")
    fun alter(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @RequestBody(required = true)
        restaurantInputModel: RestaurantInputModel
    ): RestaurantModel

    @Operation(summary = "Delete Restaurant by Id")
    fun delete(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(summary = "Activate Restaurant by Id")
    fun activate(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(summary = "Inactivate Restaurant by Id")
    fun inactivate(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(summary = "Open Restaurant by Id")
    fun open(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(summary = "Close Restaurant by Id")
    fun close(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>
}
