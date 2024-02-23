package br.com.blifood.domain.entity

import br.com.blifood.domain.createProductImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class ProductImageTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "ProductImage should initialize with default values" {
        val productImage = ProductImage()

        productImage.id shouldBe 0
        productImage.fileName shouldBe ""
        productImage.description shouldBe null
        productImage.contentType shouldBe ""
        productImage.size shouldBe 0L
    }

    "ProductImage should initialize with provided values" {
        val productImage = createProductImage()

        productImage.id shouldBe 1
        productImage.fileName shouldBe "test_image.jpg"
        productImage.description shouldBe "Test Image"
        productImage.contentType shouldBe "image/jpeg"
        productImage.size shouldBe 1024
    }

    "ProductImage fileName should not be blank" {
        val productImage = createProductImage(fileName = "")

        val violations = validator.validate(productImage)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "fileName"
    }

    "ProductImage contentType should not be blank" {
        val productImage = createProductImage(contentType = "")

        val violations = validator.validate(productImage)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "contentType"
    }

    "ProductImage size should not be negative" {
        val productImage = createProductImage(size = -1024)

        val violations = validator.validate(productImage)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "size"
    }

    "ProductImage should be valid when all constraints are met" {
        val productImage = createProductImage()
        val violations = validator.validate(productImage)
        violations.size shouldBe 0
    }
})
