package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.RestaurantProductController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.ProductModel
import br.com.blifood.api.v1.model.input.ProductInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createProduct
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.StateNotFoundException
import br.com.blifood.domain.service.ProductService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel

class RestaurantProductControllerTest : StringSpec({
    val productService = mockk<ProductService>()
    val restaurantProductController = RestaurantProductController(productService)
    val product: Product = createProduct()
    val productId = product.id
    val restaurantId = product.restaurant.id

    fun createInputModel(product: Product) = ProductInputModel(name = product.name, description = product.description, price = product.price)

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return PagedModel of RestaurantProductImageModel" {
        val pageable: Pageable = mockk()
        every { productService.findAll(any(), any()) } returns PageImpl(listOf(product))

        val result: PagedModel<ProductModel> = restaurantProductController.findAll(restaurantId, pageable)
        result.content.size shouldBe 1
    }

    "findById should return RestaurantProductImageModel" {
        every { productService.findOrThrow(any(), any()) } returns product

        val result: ProductModel = restaurantProductController.findById(restaurantId, productId)
        result shouldBe product.toModel()
    }

    "create should return created RestaurantProductImageModel" {
        every { productService.save(any()) } returns product

        val result: ProductModel = restaurantProductController.create(restaurantId, createInputModel(product))
        result shouldBe product.toModel()
    }

    "create should throw BusinessException" {
        every { productService.save(any()) } throws StateNotFoundException()

        val exception = shouldThrow<BusinessException> { restaurantProductController.create(restaurantId, createInputModel(product)) }
        exception shouldBe BusinessException(StateNotFoundException().message)
    }

    "create should throw Exception" {
        every { productService.save(any()) } throws Throwable()

        val exception = shouldThrow<Throwable> { restaurantProductController.create(restaurantId, createInputModel(product)) }
        exception shouldBe Throwable()
    }

    "alter should return altered RestaurantProductImageModel" {
        every { productService.findOrThrow(any(), any()) } returns product
        every { productService.save(any()) } returns product

        val result: ProductModel = restaurantProductController.alter(restaurantId, productId, createInputModel(product))
        result shouldBe product.toModel()
    }

    "delete should return NO_CONTENT" {
        every { productService.delete(any(), any()) } just runs

        restaurantProductController.delete(restaurantId, productId)
        verify { productService.delete(restaurantId, productId) }
    }
})
