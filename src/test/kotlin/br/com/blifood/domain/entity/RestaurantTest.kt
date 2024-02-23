import br.com.blifood.domain.createAddress
import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.entity.User
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.Validation
import jakarta.validation.Validator
import java.math.BigDecimal

class RestaurantTest : StringSpec({

    val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    "Restaurant name should be between 1 and 80 characters" {
        val restaurant = createRestaurant(name = "")
        val violations = validator.validate(restaurant)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "name"
    }

    "Restaurant name should not exceed maximum size" {
        val restaurant = createRestaurant(name = "ThisRestaurantNameIsExcessivelyLongAndShouldExceedTheMaximumAllowedSizeOfEightyCharacters")
        val violations = validator.validate(restaurant)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "name"
    }

    "Restaurant delivery fee should be positive or zero" {
        val restaurant = createRestaurant(deliveryFee = BigDecimal.valueOf(-10.0))

        val violations = validator.validate(restaurant)
        violations.size shouldBe 1
        violations.first().propertyPath.toString() shouldBe "deliveryFee"
    }

    "Restaurant should initialize correctly" {
        val restaurant = createRestaurant()

        restaurant.id shouldBe 1
        restaurant.culinary shouldBe createCulinary()
        restaurant.address shouldBe createAddress()
        restaurant.name shouldBe "Restaurant 1"
        restaurant.deliveryFee shouldBe BigDecimal.valueOf(10.0)
        restaurant.isActive() shouldBe true
        restaurant.isOpen() shouldBe false
        restaurant.paymentsMethods shouldBe mutableSetOf()
        restaurant.managers shouldBe mutableSetOf()
        restaurant.products shouldBe mutableSetOf()
    }

    "Restaurant should activate and deactivate correctly" {
        val restaurant = createRestaurant()

        restaurant.activate()
        restaurant.isActive() shouldBe true

        restaurant.inactivate()
        restaurant.isActive() shouldBe false
    }

    "Restaurant should open and close correctly" {
        val restaurant = createRestaurant()

        restaurant.open()
        restaurant.isOpen() shouldBe true

        restaurant.close()
        restaurant.isOpen() shouldBe false
    }

    "Restaurant should add and remove payment methods correctly" {
        val restaurant = createRestaurant()

        val paymentMethod = PaymentMethod(1, "Payment Method 1")

        restaurant.addPaymentMethod(paymentMethod)
        restaurant.paymentsMethods.contains(paymentMethod) shouldBe true

        restaurant.removePaymentMethod(paymentMethod)
        restaurant.paymentsMethods.contains(paymentMethod) shouldBe false
    }

    "Restaurant should add and remove managers correctly" {
        val restaurant = createRestaurant()

        val user = User(1, "User 1")

        restaurant.addManager(user)
        restaurant.managers.contains(user) shouldBe true

        restaurant.removeManager(user)
        restaurant.managers.contains(user) shouldBe false
    }
})
