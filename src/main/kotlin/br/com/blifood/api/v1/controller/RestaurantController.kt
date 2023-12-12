package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.input.RestaurantInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.RestaurantControllerOpenApi
import br.com.blifood.core.log.logRequest
import br.com.blifood.core.log.logResponse
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.RestaurantService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
@RequestMapping("/v1/restaurants", produces = [MediaType.APPLICATION_JSON_VALUE])
class RestaurantController(
    private val restaurantService: RestaurantService
) : RestaurantControllerOpenApi {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_READ}')")
    @GetMapping
    override fun findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable): PagedModel<RestaurantModel> {
        val page = restaurantService.findAll(pageable).map { it.toModel() }
        return PagedModel.of(
            page.content,
            PagedModel.PageMetadata(page.size.toLong(), page.number.toLong(), page.totalElements),
            RestaurantModel.findAllLink(pageable, true)
        )
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_READ}')")
    @GetMapping("/{restaurantId}")
    override fun findById(@PathVariable restaurantId: Long): RestaurantModel {
        return restaurantService.findOrThrow(restaurantId).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        restaurantInputModel: RestaurantInputModel
    ): RestaurantModel {
        logger.logRequest("create", restaurantInputModel)
        return save(restaurantInputModel.toEntity()).toModel().also {
            addUriInResponseHeader(it.id)
            logger.logResponse("create", it)
        }
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{restaurantId}")
    override fun alter(
        @PathVariable restaurantId: Long,
        @Valid @RequestBody
        restaurantInputModel: RestaurantInputModel
    ): RestaurantModel {
        logger.logRequest("alter", restaurantInputModel)
        val restaurant = restaurantService.findOrThrow(restaurantId).applyModel(restaurantInputModel)
        return save(restaurant).toModel().also { logger.logResponse("alter", it) }
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable restaurantId: Long): ResponseEntity<Void> {
        restaurantService.delete(restaurantId, getSecurityContextHolderUserId())
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{restaurantId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun activate(@PathVariable restaurantId: Long): ResponseEntity<Void> {
        restaurantService.activate(restaurantId, getSecurityContextHolderUserId())
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{restaurantId}/inactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun inactivate(@PathVariable restaurantId: Long): ResponseEntity<Void> {
        restaurantService.inactivate(restaurantId, getSecurityContextHolderUserId())
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{restaurantId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun open(@PathVariable restaurantId: Long): ResponseEntity<Void> {
        restaurantService.open(restaurantId, getSecurityContextHolderUserId())
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{restaurantId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun close(@PathVariable restaurantId: Long): ResponseEntity<Void> {
        restaurantService.close(restaurantId)
        return ResponseEntity.noContent().build()
    }

    private fun save(restaurant: Restaurant): Restaurant {
        return try {
            restaurantService.save(restaurant, getSecurityContextHolderUserId())
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }
}
