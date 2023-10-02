package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.input.CulinaryInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.CollectionModel
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Culinary")
interface CulinaryControllerOpenApi {

    @Operation(
        summary = "List all Culinary",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(): CollectionModel<CulinaryModel>

    @Operation(
        summary = "Find Culinary by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        culinaryId: Long
    ): CulinaryModel

    @Operation(
        summary = "Insert a Culinary",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody(required = true)
        culinaryInputDto: CulinaryInputModel
    ): CulinaryModel

    @Operation(
        summary = "Update Culinary by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun alter(
        @Parameter(example = "1", required = true)
        culinaryId: Long,
        @RequestBody(required = true)
        culinaryInputDto: CulinaryInputModel
    ): CulinaryModel

    @Operation(
        summary = "Delete Culinary by Id",
        responses = [
            ApiResponse(responseCode = "204"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun delete(
        @Parameter(example = "1", required = true)
        culinaryId: Long
    )
}
