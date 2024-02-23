package br.com.blifood.domain.entity

import br.com.blifood.domain.createState
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class StateTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "State name should not be empty" {
        val state = createState(name = "")
        val violations = validator.validate(state)
        violations.size shouldBe 1
        val constraintViolation = violations.first()
        constraintViolation.propertyPath.toString() shouldBe "name"
    }

    "State name should have a size between 2 and 2 characters" {
        val invalidState = createState(name = "A")
        val violations = validator.validate(invalidState)
        violations.size shouldBe 1
        val constraintViolation = violations.first()
        constraintViolation.propertyPath.toString() shouldBe "name"
    }

    "State with valid name should not have violations" {
        val validState = createState()
        val violations = validator.validate(validState)
        violations.size shouldBe 0
    }
})
