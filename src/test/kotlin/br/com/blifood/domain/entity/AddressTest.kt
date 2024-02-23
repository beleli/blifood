package br.com.blifood.domain.entity

import br.com.blifood.domain.createAddress
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class AddressTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "Address should have a valid value" {
        val address = createAddress()
        val violations = validator.validate(address)
        violations.size shouldBe 0
    }

    "Address zipCode should have a valid size" {
        val address = createAddress(zipCode = "")
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "zipCode"
    }

    "Address zipCode should not exceed maximum size" {
        val address = createAddress(zipCode = "01234567890")
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "zipCode"
    }

    "Address street should have a valid size" {
        val address = createAddress(street = "")
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "street"
    }

    "Address street should not exceed maximum size" {
        val address = createAddress(street = "x".repeat(101))
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "street"
    }

    "Address number should have a valid size" {
        val address = createAddress(number = "")
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "number"
    }

    "Address number should not exceed maximum size" {
        val address = createAddress(number = "x".repeat(21))
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "number"
    }

    "Address complement should have a valid size" {
        val address = createAddress(complement = "")
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "complement"
    }

    "Address complement should not exceed maximum size" {
        val address = createAddress(complement = "x".repeat(61))
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "complement"
    }

    "Address district should have a valid size" {
        val address = createAddress(district = "")
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "district"
    }

    "Address district should not exceed maximum size" {
        val address = createAddress(district = "x".repeat(61))
        val violations = validator.validate(address)
        violations.first().propertyPath.toString() shouldBe "district"
    }
})
