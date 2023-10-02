package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.ResourceUriHelper
import br.com.blifood.api.v1.model.ProductModel
import br.com.blifood.api.v1.model.input.ProductInputModel
import br.com.blifood.api.v1.model.input.applyModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.RestaurantProductControllerOpenApi
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.ProductService
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
@RequestMapping("/v1/restaurants/{restaurantId}/products", produces = [MediaType.APPLICATION_JSON_VALUE])
class RestaurantProductController(
    private val productService: ProductService
) : RestaurantProductControllerOpenApi {

    @GetMapping
    override fun findAll(@PathVariable restaurantId: Long): CollectionModel<ProductModel> {
        return CollectionModel.of(
            productService.findAll(restaurantId).map { it.toModel() },
            ProductModel.findAllLink(restaurantId, true)
        )
    }

    @GetMapping("/{productId}")
    override fun findById(@PathVariable restaurantId: Long, @PathVariable productId: Long): ProductModel {
        return productService.findOrThrow(restaurantId, productId).toModel()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @PathVariable restaurantId: Long,
        @Valid @RequestBody
        productInputDto: ProductInputModel
    ): ProductModel {
        return save(productInputDto.toEntity(restaurantId)).toModel().also {
            ResourceUriHelper.addUriInResponseHeader(it.id)
        }
    }

    @PutMapping("/{productId}")
    override fun alter(
        @PathVariable restaurantId: Long,
        @PathVariable productId: Long,
        @Valid @RequestBody
        productInputDto: ProductInputModel
    ): ProductModel {
        val product = productService.findOrThrow(restaurantId, productId).applyModel(productInputDto)
        return save(product).toModel()
    }

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
