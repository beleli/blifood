package br.com.blifood.domain.entity

import br.com.blifood.domain.createProduct
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator
import java.math.BigDecimal

class ProductTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "Product should initialize with default values" {
        val product = Product()
        val restaurant = product.restaurant

        product.id shouldBe 0
        product.restaurant shouldBe restaurant
        product.name shouldBe ""
        product.description shouldBe ""
        product.price shouldBe BigDecimal.ZERO
        product.active shouldBe true
    }

    "Product name should not be blank" {
        val product = createProduct(name = "")

        val violations = validator.validate(product)
        violations.size shouldBe 1
    }

    "Product description should not be blank" {
        val product = createProduct(description = "")

        val violations = validator.validate(product)
        violations.size shouldBe 1
    }

    "Product price should not be negative" {
        val product = createProduct(price = BigDecimal.valueOf(-10.0))

        val violations = validator.validate(product)
        violations.size shouldBe 1
    }

    "Product should be valid when all constraints are met" {
        val product = createProduct(
            name = "Test Product",
            description = "Description",
            price = BigDecimal.valueOf(10.0)
        )

        val violations = validator.validate(product)
        violations.size shouldBe 0
    }
})
