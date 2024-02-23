package br.com.blifood.domain.entity

import br.com.blifood.domain.createUser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class UserTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "User should initialize with default values" {
        val user = User()

        user.id shouldBe 0
        user.name shouldBe ""
        user.email shouldBe ""
        user.password shouldBe ""
        user.profile shouldBe UserProfile.CUSTOMER
    }

    "User should initialize with provided values" {
        val user = createUser()

        user.id shouldBe 1
        user.name shouldBe "Test User"
        user.email shouldBe "test@example.com"
        user.password shouldBe "password"
        user.profile shouldBe UserProfile.ADMIN
    }

    "User name should not be empty" {
        val user = createUser(name = "")

        val violations = validator.validate(user)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "name"
    }

    "User email should not be empty" {
        val user = createUser(email = "")

        val violations = validator.validate(user)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "email"
    }

    "User email should not be invalid" {
        val user = createUser(email = "test")

        val violations = validator.validate(user)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "email"
    }

    "User password should not be empty" {
        val user = createUser(password = "")

        val violations = validator.validate(user)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "password"
    }
})
