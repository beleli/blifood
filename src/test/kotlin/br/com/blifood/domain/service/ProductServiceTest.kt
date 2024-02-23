package br.com.blifood.domain.service

import br.com.blifood.domain.createProduct
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.exception.ProductAlreadyExistsException
import br.com.blifood.domain.exception.ProductInUseException
import br.com.blifood.domain.exception.ProductNotFoundException
import br.com.blifood.domain.repository.ProductRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

class ProductServiceTest : DescribeSpec({

    val productRepository = mockk<ProductRepository>()
    val restaurantService = mockk<RestaurantService>()
    val productService = ProductService(productRepository, restaurantService)

    val product = createProduct()
    val productId = product.id
    val restaurant = product.restaurant
    val restaurantId = product.restaurant.id

    describe("findAll") {
        val pageable = mockk<Pageable>()
        val page = mockk<Page<Product>>()

        it("should return all products for a restaurant") {
            every { productRepository.findByRestaurantId(any(), any()) } returns page

            val result = productService.findAll(restaurantId, pageable)

            result shouldBe page
            verify { productRepository.findByRestaurantId(restaurantId, pageable) }
        }
    }

    describe("findOrThrow") {
        it("should return product if found for a restaurant") {
            every { productRepository.findByIdAndRestaurantId(any(), any()) } returns Optional.of(product)

            val result = productService.findOrThrow(restaurantId, productId)

            result shouldBe product
            verify { productRepository.findByIdAndRestaurantId(productId, restaurantId) }
        }

        it("should throw ProductNotFoundException if product not found for a restaurant") {
            every { productRepository.findByIdAndRestaurantId(any(), any()) } returns Optional.empty()

            val exception = shouldThrowExactly<ProductNotFoundException> { productService.findOrThrow(restaurantId, productId) }

            exception shouldBe ProductNotFoundException()
            verify { productRepository.findByIdAndRestaurantId(productId, restaurantId) }
        }
    }

    describe("save") {
        it("should save product for a restaurant") {
            every { restaurantService.findOrThrow(any()) } returns restaurant
            every { productRepository.findByNameAndRestaurantId(any(), any()) } returns null
            every { productRepository.save(any()) } returns product

            val result = productService.save(product)

            result shouldBe product
            verify { productRepository.save(product) }
        }

        it("should throw ProductAlreadyExistsException if product with same name already exists for a restaurant") {
            every { restaurantService.findOrThrow(any()) } returns restaurant
            every { productRepository.findByNameAndRestaurantId(any(), any()) } returns createProduct(id = 0)

            val exception = shouldThrowExactly<ProductAlreadyExistsException> { productService.save(product) }

            exception shouldBe ProductAlreadyExistsException()
            verify { productRepository.findByNameAndRestaurantId(product.name, restaurantId) }
        }
    }

    describe("delete") {
        it("should delete product for a restaurant") {
            every { productRepository.findByIdAndRestaurantId(any(), any()) } returns Optional.of(product)
            every { productRepository.delete(any()) } just runs
            every { productRepository.flush() } just runs

            productService.delete(restaurantId, productId)

            verify { productRepository.delete(product) }
            verify { productRepository.flush() }
        }

        it("should throw ProductInUseException if product is in use") {
            every { productRepository.findByIdAndRestaurantId(any(), any()) } returns Optional.of(product)
            every { productRepository.delete(any()) } throws DataIntegrityViolationException("Violation")

            val exception = shouldThrowExactly<ProductInUseException> { productService.delete(restaurantId, productId) }

            exception shouldBe ProductInUseException()
            verify { productRepository.delete(product) }
        }
    }
})
