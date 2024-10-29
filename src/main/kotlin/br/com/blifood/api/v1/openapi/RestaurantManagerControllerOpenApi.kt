package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.UserModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.CollectionModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "Restaurants")
interface RestaurantManagerControllerOpenApi {

    @Operation(
        summary = "List all Managers of a restaurant",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(
        @PathVariable restaurantId: Long
    ): CollectionModel<UserModel>

    @Operation(
        summary = "Add Manager to the Restaurant",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun add(
        @Parameter(example = "1", required = true)
        restaurantId: Long,
        @Parameter(example = "1", required = true)
        userId: Long?
    ): ResponseEntity<Unit>

    @Operation(
        summary = "Remove Manager to the Restaurant",
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
        userId: Long
    ): ResponseEntity<Unit>
}
