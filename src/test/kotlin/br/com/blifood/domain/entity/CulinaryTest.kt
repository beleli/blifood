import br.com.blifood.domain.createCulinary
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class CulinaryTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "Culinary name should have a valid size" {
        val culinary = createCulinary()
        val violations = validator.validate(culinary)
        violations.size shouldBe 0
    }

    "Culinary name should not exceed maximum size" {
        val culinary = createCulinary(name = "ThisCulinaryNameIsExcessivelyLongAndShouldExceedTheMaximumAllowedSizeOfSixtyCharacters")
        val violations = validator.validate(culinary)
        violations.size shouldBe 1
        val constraintViolation = violations.first()
        constraintViolation.propertyPath.toString() shouldBe "name"
    }
})
