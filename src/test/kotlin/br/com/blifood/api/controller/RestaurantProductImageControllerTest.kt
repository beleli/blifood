package br.com.blifood.api.controller

import br.com.blifood.api.v1.controller.RestaurantProductImageController
import br.com.blifood.api.v1.model.input.ProductImageInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createProduct
import br.com.blifood.domain.createProductImage
import br.com.blifood.domain.service.ProductImageService
import br.com.blifood.domain.service.ProductService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.net.URL

class RestaurantProductImageControllerTest : StringSpec({

    val productService = mockk<ProductService>()
    val productImageService = mockk<ProductImageService>()
    val controller = RestaurantProductImageController(productService, productImageService)

    val product = createProduct()
    val productImage = createProductImage()
    val productId = product.id
    val restaurantId = product.restaurant.id

    fun createInputModel() = ProductImageInputModel(
        file = mockk<MultipartFile>(relaxed = true),
        description = "description of the product"
    )

    "findById should return ProductImageModel" {
        every { productImageService.findOrThrow(any()) } returns productImage

        val result = controller.findById(restaurantId, productId)
        result shouldBe productImage.toModel(restaurantId)
    }

    "download should return ResponseEntity with image URL" {
        val imageUrl = URL("https://example.com/image.jpg")
        every { productImageService.findOrThrow(any()) } returns productImage
        every { productImageService.recoverImage(any()) } returns imageUrl

        val result = controller.download(restaurantId, productId) as ResponseEntity<*>
        result.statusCode shouldBe HttpStatus.FOUND
        result.headers.location shouldBe imageUrl.toURI()
    }

    "download should return ResponseEntity with image InputStream" {
        val imageInputStream = mockk<InputStream>()
        every { productImageService.findOrThrow(any()) } returns productImage
        every { productImageService.recoverImage(any()) } returns imageInputStream

        val result = controller.download(restaurantId, productId) as ResponseEntity<*>

        result.statusCode shouldBe HttpStatus.OK
        result.headers.contentType shouldBe MediaType.IMAGE_JPEG
    }

    "upload should return ProductImageModel" {
        every { productService.findOrThrow(any(), any()) } returns product
        every { productImageService.save(any(), any()) } returns productImage

        val result = controller.upload(restaurantId, productId, createInputModel())
        result shouldBe productImage.toModel(restaurantId)
    }

    "remove should return NO_CONTENT" {
        every { productService.findOrThrow(any(), any()) } returns product
        every { productImageService.delete(any()) } answers {}

        controller.remove(restaurantId, productId)
        verify { productImageService.delete(productId) }
    }
})
