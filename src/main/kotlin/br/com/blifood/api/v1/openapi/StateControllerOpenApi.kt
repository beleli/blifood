package br.com.blifood.api.v1.openapi

import br.com.blifood.api.v1.model.StateModel
import br.com.blifood.api.v1.model.input.StateInputModel
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

@Tag(name = "States")
interface StateControllerOpenApi {

    @PageableParameter
    @Operation(
        summary = "List all States",
        responses = [
            ApiResponse(responseCode = "200")
        ]
    )
    fun findAll(@Parameter(hidden = true) pageable: Pageable): PagedModel<StateModel>

    @Operation(
        summary = "Find State by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun findById(
        @Parameter(example = "1", required = true)
        stateId: Long
    ): StateModel

    @Operation(
        summary = "Insert a State",
        responses = [
            ApiResponse(responseCode = "201"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun create(
        @RequestBody(required = true)
        stateInputModel: StateInputModel
    ): StateModel

    @Operation(
        summary = "Update State by Id",
        responses = [
            ApiResponse(responseCode = "200"),
            ApiResponse(responseCode = "400", content = [Content(schema = Schema(ref = "ApiProblemDetail"))]),
            ApiResponse(responseCode = "404", content = [Content(schema = Schema(ref = "ApiProblemDetail"))])
        ]
    )
    fun alter(
        @Parameter(example = "1", required = true)
        stateId: Long,
        @RequestBody(required = true)
        stateInputModel: StateInputModel
    ): StateModel

    @Operation(
        summary = "Delete State by Id",
        responses = [
            ApiResponse(responseCode = "204")
        ]
    )
    fun delete(
        @Parameter(example = "1", required = true)
        stateId: Long
    )
}
