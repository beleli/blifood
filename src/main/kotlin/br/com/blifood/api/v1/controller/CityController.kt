package br.com.blifood.api.v1.controller

import br.com.blifood.api.aspect.LogAndValidate
import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.input.CityInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.CityControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.CityService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.access.prepost.PreAuthorize
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

    @LogAndValidate(validateRequest = false, logResponse = false)
    @PreAuthorize("hasAuthority('${Authority.CITY_READ}')")
    @GetMapping
    override fun findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable): PagedModel<CityModel> {
        val page = cityService.findAll(pageable).map { it.toModel() }
        return PagedModel.of(
            page.content,
            PagedModel.PageMetadata(page.size.toLong(), page.number.toLong(), page.totalElements),
            CityModel.findAllLink(pageable, true)
        )
    }

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.CITY_READ}')")
    @GetMapping("/{cityId}")
    override fun findById(@PathVariable cityId: Long): CityModel {
        return cityService.findOrThrow(cityId).toModel()
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.CITY_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @RequestBody
        cityInputModel: CityInputModel
    ): CityModel {
        return save(cityInputModel.toEntity()).toModel().also {
            addUriInResponseHeader(it.id)
        }
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.CITY_WRITE}')")
    @PutMapping("/{cityId}")
    override fun alter(
        @PathVariable cityId: Long,
        @RequestBody
        cityInputModel: CityInputModel
    ): CityModel {
        val city = cityService.findOrThrow(cityId).copy().applyModel(cityInputModel)
        return save(city).toModel()
    }

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.CITY_WRITE}')")
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
