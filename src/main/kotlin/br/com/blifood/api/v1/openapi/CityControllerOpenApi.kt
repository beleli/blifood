package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.input.CityInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.CollectionModel

@Tag(name = "Cities")
interface CityControllerOpenApi {

    @Operation(
        summary = "List all Cities",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(): CollectionModel<CityModel>

    @Operation(
        summary = "Find City by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        cityId: Long
    ): CityModel

    @Operation(
        summary = "Insert a City",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody(description = "Representação de uma nova cidade", required = true)
        cityInputModel: CityInputModel
    ): CityModel

    @Operation(
        summary = "Update City by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun alter(
        @Parameter(example = "1", required = true)
        cityId: Long,
        @RequestBody(required = true)
        cityInputModel: CityInputModel
    ): CityModel

    @Operation(
        summary = "Delete City by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun delete(
        @Parameter(example = "1", required = true)
        cityId: Long
    )
}
