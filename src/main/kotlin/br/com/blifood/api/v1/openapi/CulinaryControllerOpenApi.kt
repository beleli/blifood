package br.com.blifood.api.v1.openapi

import br.com.blifood.api.openapi.PageableParameter
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.input.CulinaryInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Culinary")
interface CulinaryControllerOpenApi {

    @PageableParameter
    @Operation(summary = "List all Culinary")
    fun findAll(@Parameter(hidden = true) pageable: Pageable): PagedModel<CulinaryModel>

    @Operation(summary = "Find Culinary by Id")
    fun findById(
        @Parameter(example = "1", required = true)
        culinaryId: Long
    ): CulinaryModel

    @Operation(summary = "Insert a Culinary")
    fun create(
        @RequestBody(required = true)
        culinaryInputModel: CulinaryInputModel
    ): CulinaryModel

    @Operation(summary = "Update Culinary by Id")
    fun alter(
        @Parameter(example = "1", required = true)
        culinaryId: Long,
        @RequestBody(required = true)
        culinaryInputModel: CulinaryInputModel
    ): CulinaryModel

    @Operation(summary = "Delete Culinary by Id")
    fun delete(
        @Parameter(example = "1", required = true)
        culinaryId: Long
    )
}
