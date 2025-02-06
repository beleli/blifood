package br.com.blifood.api.v1.openapi

import br.com.blifood.api.openapi.PageableParameter
import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.input.CityInputModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.CollectionModel

@Tag(name = "Cities")
interface CityControllerOpenApi {

    @PageableParameter
    @Operation(summary = "List all Cities")
    fun findAll(@Parameter(hidden = true) pageable: Pageable): CollectionModel<CityModel>

    @Operation(summary = "Find City by Id")
    fun findById(
        @Parameter(example = "1", required = true)
        cityId: Long
    ): CityModel

    @Operation(summary = "Insert a City")
    fun create(
        @RequestBody(required = true)
        cityInputModel: CityInputModel
    ): CityModel

    @Operation(summary = "Update City by Id")
    fun alter(
        @Parameter(example = "1", required = true)
        cityId: Long,
        @RequestBody(required = true)
        cityInputModel: CityInputModel
    ): CityModel

    @Operation(summary = "Delete City by Id")
    fun delete(
        @Parameter(example = "1", required = true)
        cityId: Long
    )
}
