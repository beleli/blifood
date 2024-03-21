package br.com.blifood.api.v1.controller

import br.com.blifood.api.aspect.LogAndValidate
import br.com.blifood.api.v1.model.UserModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.RestaurantManagerControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.RestaurantService
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/restaurants/{restaurantId}/managers", produces = [MediaType.APPLICATION_JSON_VALUE])
class RestaurantManagerController(
    private val restaurantService: RestaurantService
) : RestaurantManagerControllerOpenApi {

    @LogAndValidate(validateRequest = false, logResponse = false)
    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @GetMapping
    override fun findAll(
        @PathVariable restaurantId: Long
    ): CollectionModel<UserModel> {
        try {
            return CollectionModel.of(
                restaurantService.findAllManagers(restaurantId).map { it.toModel() }
            )
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun add(
        @PathVariable restaurantId: Long,
        @PathVariable userId: Long?
    ): ResponseEntity<Void> {
        try {
            restaurantService.addManager(restaurantId, userId!!)
            return ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }

    @LogAndValidate(validateRequest = false)
    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun remove(
        @PathVariable restaurantId: Long,
        @PathVariable userId: Long
    ): ResponseEntity<Void> {
        try {
            restaurantService.removeManager(restaurantId, userId)
            return ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }
}
