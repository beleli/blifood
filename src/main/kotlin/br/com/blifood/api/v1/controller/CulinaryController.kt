package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.ResourceUriHelper
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.input.CulinaryInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.CulinaryControllerOpenApi
import br.com.blifood.domain.service.CulinaryService
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
@RequestMapping("/v1/culinary", produces = [MediaType.APPLICATION_JSON_VALUE])
class CulinaryController(
    private val culinaryService: CulinaryService
) : CulinaryControllerOpenApi {

    @GetMapping
    override fun findAll(): CollectionModel<CulinaryModel> {
        return CollectionModel.of(
            culinaryService.findAll().map { it.toModel() },
            CulinaryModel.findAllLink(true)
        )
    }

    @GetMapping("/{culinaryId}")
    override fun findById(@PathVariable culinaryId: Long): CulinaryModel {
        return culinaryService.findOrThrow(culinaryId).toModel()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        culinaryInputDto: CulinaryInputModel
    ): CulinaryModel {
        return culinaryService.save(culinaryInputDto.toEntity()).toModel().also {
            ResourceUriHelper.addUriInResponseHeader(it.id)
        }
    }

    @PutMapping("/{culinaryId}")
    override fun alter(
        @PathVariable culinaryId: Long,
        @Valid @RequestBody
        culinaryInputDto: CulinaryInputModel
    ): CulinaryModel {
        val culinary = culinaryService.findOrThrow(culinaryId).applyModel(culinaryInputDto)
        return culinaryService.save(culinary).toModel()
    }

    @DeleteMapping("/{culinaryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable culinaryId: Long) {
        culinaryService.delete(culinaryId)
    }
}
