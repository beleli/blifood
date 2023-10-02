package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.ResourceUriHelper
import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.input.CityInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.CityControllerOpenApi
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.CityService
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/cities", produces = [APPLICATION_JSON_VALUE])
class CityController(
    private val cityService: CityService
) : CityControllerOpenApi {

    @GetMapping
    override fun findAll(): CollectionModel<CityModel> {
        return CollectionModel.of(
            cityService.findAll().map { it.toModel() },
            CityModel.findAllLink(true)
        )
    }

    @GetMapping("/{cityId}")
    override fun findById(@PathVariable cityId: Long): CityModel {
        return cityService.findOrThrow(cityId).toModel()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        cityInputDto: CityInputModel
    ): CityModel {
        val city = cityInputDto.toEntity()
        return save(city).toModel().also {
            ResourceUriHelper.addUriInResponseHeader(it.id)
        }
    }

    @PutMapping("/{cityId}")
    override fun alter(
        @PathVariable cityId: Long,
        @Valid @RequestBody
        cityInputDto: CityInputModel
    ): CityModel {
        val city = cityService.findOrThrow(cityId).applyModel(cityInputDto)
        return save(city).toModel()
    }

    @DeleteMapping("/{cityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable cityId: Long) {
        cityService.delete(cityId)
    }

    private fun save(city: City): City {
        return try {
            cityService.save(city)
        } catch (ex: EntityNotFoundException) {
            throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }
}
