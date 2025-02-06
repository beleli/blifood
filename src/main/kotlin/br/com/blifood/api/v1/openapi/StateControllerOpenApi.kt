package br.com.blifood.api.v1.openapi

import br.com.blifood.api.openapi.PageableParameter
import br.com.blifood.api.v1.model.StateModel
import br.com.blifood.api.v1.model.input.StateInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "States")
interface StateControllerOpenApi {

    @PageableParameter
    @Operation(summary = "List all States")
    fun findAll(@Parameter(hidden = true) pageable: Pageable): PagedModel<StateModel>

    @Operation(summary = "Find State by Id")
    fun findById(
        @Parameter(example = "1", required = true)
        stateId: Long
    ): StateModel

    @Operation(summary = "Insert a State")
    fun create(
        @RequestBody(required = true)
        stateInputModel: StateInputModel
    ): StateModel

    @Operation(summary = "Update State by Id")
    fun alter(
        @Parameter(example = "1", required = true)
        stateId: Long,
        @RequestBody(required = true)
        stateInputModel: StateInputModel
    ): StateModel

    @Operation(summary = "Delete State by Id")
    fun delete(
        @Parameter(example = "1", required = true)
        stateId: Long
    )
}
