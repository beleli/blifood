package br.com.blifood.api.v1.openapi

import br.com.blifood.api.openapi.PageableParameter
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.input.RestaurantInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Restaurants")
interface RestaurantControllerOpenApi {

    @PageableParameter
    @Operation(
        summary = "List all Restaurants",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(@Parameter(hidden = true) pageable: Pageable): PagedModel<RestaurantModel>

    @Operation(
        summary = "Find Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): RestaurantModel

    @Operation(
        summary = "Insert a Restaurant",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody(required = true)
        restaurantInputModel: RestaurantInputModel
    ): RestaurantModel

    @Operation(
        summary = "Update Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun alter(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @RequestBody(required = true)
        restaurantInputModel: RestaurantInputModel
    ): RestaurantModel

    @Operation(
        summary = "Delete Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun delete(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Activate Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun activate(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Inactivate Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun inactivate(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Open Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun open(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Close Restaurant by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun close(
        @Parameter(example = "1", required = true)
        restaurantId: Long
    ): ResponseEntity<Unit>
}
