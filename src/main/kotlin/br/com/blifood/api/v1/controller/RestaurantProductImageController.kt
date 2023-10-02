package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.model.ProductImageModel
import br.com.blifood.api.v1.model.input.ProductImageInputModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.RestaurantProductImageControllerOpenApi
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.ProductImageService
import br.com.blifood.domain.service.ProductService
import jakarta.validation.Valid
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream
import java.net.URL

@RestController
@RequestMapping("/v1/restaurants/{restaurantId}/products/{productId}/images", produces = [MediaType.APPLICATION_JSON_VALUE])
class RestaurantProductImageController(
    private val productService: ProductService,
    private val productImageService: ProductImageService
) : RestaurantProductImageControllerOpenApi {

    @GetMapping
    override fun findById(@PathVariable restaurantId: Long, @PathVariable productId: Long): ProductImageModel {
        return productImageService.findOrThrow(productId).toModel()
    }

    @GetMapping(produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE])
    fun download(@PathVariable restaurantId: Long, @PathVariable productId: Long): Any {
        val productImage = productImageService.findOrThrow(productId).toModel()
        val image = productImageService.recoverImage(productImage.fileName)
        return if (image is URL) {
            ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, image.toString())
                .build()
        } else {
            image as InputStream
            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(productImage.contentType))
                .body<InputStreamResource>(InputStreamResource(image))
        }
    }

    @PutMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun upload(
        @PathVariable restaurantId: Long,
        @PathVariable productId: Long,
        @Valid productImageInput: ProductImageInputModel
    ): ProductImageModel {
        val product = findProductOrThrow(restaurantId, productId)
        val productImage = productImageInput.toEntity(product)
        return productImageService.save(productImage, productImageInput.file!!.inputStream).toModel()
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun remove(@PathVariable restaurantId: Long, @PathVariable productId: Long) {
        findProductOrThrow(restaurantId, productId)
        productImageService.delete(restaurantId, productId)
    }

    private fun findProductOrThrow(restaurantId: Long, productId: Long): Product {
        return try {
            productService.findOrThrow(restaurantId, productId)
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Exception) {
            throw ex
        }
    }
}
