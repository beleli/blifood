package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.model.StateModel
import br.com.blifood.api.v1.model.input.StateInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.StateControllerOpenApi
import br.com.blifood.core.log.logRequest
import br.com.blifood.core.log.logResponse
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.service.StateService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
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
@RequestMapping("/v1/states", produces = [MediaType.APPLICATION_JSON_VALUE])
class StateController(
    private val stateService: StateService
) : StateControllerOpenApi {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PreAuthorize("hasAuthority('${Authority.STATE_READ}')")
    @GetMapping
    override fun findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable): PagedModel<StateModel> {
        val page = stateService.findAll(pageable).map { it.toModel() }
        return PagedModel.of(
            page.content,
            PagedModel.PageMetadata(page.size.toLong(), page.number.toLong(), page.totalElements),
            StateModel.findAllLink(pageable, true)
        )
    }

    @PreAuthorize("hasAuthority('${Authority.STATE_READ}')")
    @GetMapping("/{stateId}")
    override fun findById(@PathVariable stateId: Long): StateModel {
        return stateService.findOrThrow(stateId).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.STATE_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        stateInputModel: StateInputModel
    ): StateModel {
        logger.logRequest("create", stateInputModel)
        return stateService.save(stateInputModel.toEntity()).toModel().also {
            addUriInResponseHeader(it.id)
            logger.logResponse("create", it)
        }
    }

    @PreAuthorize("hasAuthority('${Authority.STATE_WRITE}')")
    @PutMapping("/{stateId}")
    override fun alter(
        @PathVariable stateId: Long,
        @Valid @RequestBody
        stateInputModel: StateInputModel
    ): StateModel {
        logger.logRequest("alter", stateInputModel)
        val state = stateService.findOrThrow(stateId).applyModel(stateInputModel)
        return stateService.save(state).toModel().also { logger.logResponse("alter", it) }
    }

    @PreAuthorize("hasAuthority('${Authority.STATE_WRITE}')")
    @DeleteMapping("/{stateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable stateId: Long) {
        stateService.delete(stateId)
    }
}
