import br.com.blifood.domain.createPaymentMethod
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator

class PaymentMethodTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "PaymentMethod description should have a valid size" {
        val paymentMethod = createPaymentMethod()
        val violations = validator.validate(paymentMethod)
        violations.size shouldBe 0
    }

    "PaymentMethod description should not exceed maximum size" {
        val paymentMethod =
            createPaymentMethod(
                description = "ThisPaymentMethodDescriptionIsExcessivelyLongAndShouldExceedTheMaximumAllowedSizeOfSixtyCharacters"
            )
        val violations = validator.validate(paymentMethod)
        violations.size shouldBe 1
        val constraintViolation = violations.first()
        constraintViolation.propertyPath.toString() shouldBe "description"
    }
})
