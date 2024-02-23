package br.com.blifood.domain.entity

import br.com.blifood.domain.createCity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class CityTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "City name should have a valid value" {
        val city = createCity()
        val violations = validator.validate(city)
        violations.size shouldBe 0
    }

    "City name should not exceed maximum size" {
        val city = createCity(name = "ThisCityNameIsExcessivelyLongAndShouldExceedTheMaximumAllowedSizeOfEightyCharacters")
        val violations = validator.validate(city)
        violations.size shouldBe 1
        val constraintViolation = violations.first()
        constraintViolation.propertyPath.toString() shouldBe "name"
    }

    "City state should not be null" {
        val city = createCity(state = State())
        val violations = validator.validate(city)
        violations.size shouldBe 1
    }
})
