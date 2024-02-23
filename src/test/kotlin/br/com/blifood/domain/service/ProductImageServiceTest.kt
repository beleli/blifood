package br.com.blifood.domain.service

import br.com.blifood.domain.createProductImage
import br.com.blifood.domain.exception.ProductImageNotFoundException
import br.com.blifood.domain.repository.ProductImageRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import java.io.InputStream
import java.util.Optional

class ProductImageServiceTest : DescribeSpec({

    val productImageRepository = mockk<ProductImageRepository>()
    val imageStorageService = mockk<ImageStorageService>()
    val productImageService = ProductImageService(productImageRepository, imageStorageService)

    val productImage = createProductImage()
    val productImageId = productImage.id

    describe("findOrThrow") {
        it("should return product image when found") {
            every { productImageRepository.findById(any()) } returns Optional.of(productImage)

            val result = productImageService.findOrThrow(productImageId)

            result shouldBe productImage
        }

        it("should throw exception when product image not found") {
            every { productImageRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<ProductImageNotFoundException> { productImageService.findOrThrow(productImageId) }

            exception shouldBe ProductImageNotFoundException()
        }
    }

    describe("save") {
        it("should save product image and upload file") {
            every { productImageRepository.findById(any()) } returns Optional.of(productImage)
            every { imageStorageService.upload(any()) } just runs
            every { imageStorageService.remove(any()) } just runs
            every { productImageRepository.save(any()) } returns productImage

            val fileData = mockk<InputStream>()
            productImageService.save(productImage, fileData)

            verify { imageStorageService.remove(productImage.fileName) }
            verify { imageStorageService.upload(any()) }
            verify { productImageRepository.save(productImage) }
        }
    }

    describe("delete") {
        it("should delete product image and remove file") {
            every { productImageRepository.findById(any()) } returns Optional.of(productImage)
            every { imageStorageService.remove(any()) } just runs
            every { imageStorageService.upload(any()) } just runs
            every { productImageRepository.delete(any()) } just runs

            productImageService.delete(productImageId)

            verify { imageStorageService.remove(productImage.fileName) }
            verify { productImageRepository.delete(productImage) }
        }

        it("should throw exception when product image not found") {
            every { productImageRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<ProductImageNotFoundException> { productImageService.delete(productImageId) }

            exception shouldBe ProductImageNotFoundException()
        }
    }

    describe("recoverImage") {
        it("should recover image file") {
            every { imageStorageService.recover(any()) } returns Any()

            productImageService.recoverImage(productImage.fileName)

            verify { imageStorageService.recover(productImage.fileName) }
        }
    }
})
