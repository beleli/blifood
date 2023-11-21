package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.model.ProductModel
import br.com.blifood.api.v1.model.input.ProductInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.RestaurantProductControllerOpenApi
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.ProductService
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
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
@RequestMapping("/v1/restaurants/{restaurantId}/products", produces = [MediaType.APPLICATION_JSON_VALUE])
class RestaurantProductController(
    private val productService: ProductService
) : RestaurantProductControllerOpenApi {

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_READ}')")
    @GetMapping
    override fun findAll(@PathVariable restaurantId: Long): CollectionModel<ProductModel> {
        return CollectionModel.of(
            productService.findAll(restaurantId).map { it.toModel() },
            ProductModel.findAllLink(restaurantId, true)
        )
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_READ}')")
    @GetMapping("/{productId}")
    override fun findById(@PathVariable restaurantId: Long, @PathVariable productId: Long): ProductModel {
        return productService.findOrThrow(restaurantId, productId).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @PathVariable restaurantId: Long,
        @Valid @RequestBody
        productInputModel: ProductInputModel
    ): ProductModel {
        return save(productInputModel.toEntity(restaurantId)).toModel().also {
            addUriInResponseHeader(it.id)
        }
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @PutMapping("/{productId}")
    override fun alter(
        @PathVariable restaurantId: Long,
        @PathVariable productId: Long,
        @Valid @RequestBody
        productInputModel: ProductInputModel
    ): ProductModel {
        val product = productService.findOrThrow(restaurantId, productId).applyModel(productInputModel)
        return save(product).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.RESTAURANT_WRITE}')")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable restaurantId: Long, @PathVariable productId: Long) {
        productService.delete(restaurantId, productId)
    }

    private fun save(product: Product): Product {
        return try {
            productService.save(product)
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Exception) {
            throw ex
        }
    }
}
