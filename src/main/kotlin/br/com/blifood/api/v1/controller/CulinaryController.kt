package br.com.blifood.api.v1.controller

import br.com.blifood.api.aspect.LogAndValidate
import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.input.CulinaryInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.CulinaryControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.service.CulinaryService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
@RequestMapping("/v1/culinary", produces = [MediaType.APPLICATION_JSON_VALUE])
class CulinaryController(
    private val culinaryService: CulinaryService
) : CulinaryControllerOpenApi {

    @LogAndValidate(validateRequest = false, logResponse = false)
    @PreAuthorize("hasAuthority('${Authority.CULINARY_READ}')")
    @GetMapping
    override fun findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable): PagedModel<CulinaryModel> {
        val page = culinaryService.findAll(pageable).map { it.toModel() }
        return PagedModel.of(
            page.content,
            PagedModel.PageMetadata(page.size.toLong(), page.number.toLong(), page.totalElements),
            CulinaryModel.findAllLink(pageable, true)
        )
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.CULINARY_READ}')")
    @GetMapping("/{culinaryId}")
    override fun findById(@PathVariable culinaryId: Long): CulinaryModel {
        return culinaryService.findOrThrow(culinaryId).toModel()
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.CULINARY_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @RequestBody
        culinaryInputModel: CulinaryInputModel
    ): CulinaryModel {
        return culinaryService.save(culinaryInputModel.toEntity()).toModel().also {
            addUriInResponseHeader(it.id)
        }
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.CULINARY_WRITE}')")
    @PutMapping("/{culinaryId}")
    override fun alter(
        @PathVariable culinaryId: Long,
        @RequestBody
        culinaryInputModel: CulinaryInputModel
    ): CulinaryModel {
        val culinary = culinaryService.findOrThrow(culinaryId).copy().applyModel(culinaryInputModel)
        return culinaryService.save(culinary).toModel()
    }

    @LogAndValidate
    @PreAuthorize("hasAuthority('${Authority.CULINARY_WRITE}')")
    @DeleteMapping("/{culinaryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable culinaryId: Long) {
        culinaryService.delete(culinaryId)
    }
}
