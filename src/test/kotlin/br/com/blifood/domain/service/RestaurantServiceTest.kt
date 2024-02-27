package br.com.blifood.domain.service

import br.com.blifood.domain.createPaymentMethod
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.exception.RestaurantAlreadyExistsException
import br.com.blifood.domain.exception.RestaurantInUseException
import br.com.blifood.domain.exception.RestaurantNotFoundException
import br.com.blifood.domain.exception.UserNotAuthorizedException
import br.com.blifood.domain.repository.RestaurantRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional

class RestaurantServiceTest : DescribeSpec({

    val restaurantRepository = mockk<RestaurantRepository>()
    val cityService = mockk<CityService>()
    val culinaryService = mockk<CulinaryService>()
    val paymentMethodService = mockk<PaymentMethodService>()
    val userService = mockk<UserService>()

    val restaurantService = RestaurantService(
        restaurantRepository,
        cityService,
        culinaryService,
        paymentMethodService,
        userService
    )

    val restaurant = createRestaurant()
    val restaurantId = restaurant.id
    val culinary = restaurant.culinary
    val city = restaurant.address.city
    val user = createUser()
    val userId = user.id
    val paymentMethod = createPaymentMethod()
    val paymentMethodId = paymentMethod.id

    describe("findAll") {
        it("should return page of restaurants") {
            val pageable = mockk<Pageable>()
            val restaurants = listOf(createRestaurant())
            val page = PageImpl(restaurants)
            every { restaurantRepository.findAll(pageable) } returns page

            val result = restaurantService.findAll(pageable)

            result shouldBe page
        }
    }

    describe("findOrThrow") {
        it("should return restaurant if found") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)

            val result = restaurantService.findOrThrow(restaurantId)

            result shouldBe restaurant
        }

        it("should throw exception if restaurant not found") {
            every { restaurantRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<RestaurantNotFoundException> { restaurantService.findOrThrow(restaurantId) }

            exception shouldBe RestaurantNotFoundException()
        }
    }

    describe("save") {
        it("should save a restaurant") {
            every { userService.findOrThrow(any()) } returns user
            every { culinaryService.findOrThrow(any()) } returns culinary
            every { cityService.findOrThrow(any()) } returns city
            every { restaurantRepository.findByNameAndAddressCityId(any(), any()) } returns null
            every { restaurantRepository.save(any()) } returns restaurant

            val result = restaurantService.save(restaurant, userId)

            result shouldBe restaurant
            verify { restaurantRepository.save(restaurant) }
        }

        it("should throw exception when trying to save a restaurant with existing name") {
            every { userService.findOrThrow(any()) } returns user
            every { culinaryService.findOrThrow(any()) } returns culinary
            every { cityService.findOrThrow(any()) } returns city
            every { restaurantRepository.findByNameAndAddressCityId(any(), any()) } returns restaurant

            val newRestaurant = createRestaurant(id = 0)
            val exception = shouldThrow<RestaurantAlreadyExistsException> { restaurantService.save(newRestaurant, userId) }

            exception shouldBe RestaurantAlreadyExistsException()
            verify(exactly = 0) { restaurantRepository.save(newRestaurant) }
        }
    }

    describe("delete") {
        beforeEach { clearAllMocks() }

        it("should delete a restaurant") {
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { restaurantRepository.deleteById(any()) } just runs
            every { restaurantRepository.flush() } just runs

            restaurantService.delete(restaurantId, userId)

            verify { restaurantRepository.deleteById(restaurantId) }
        }

        it("should throw exception when trying to delete a non-existent restaurant") {
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<RestaurantNotFoundException> { restaurantService.delete(restaurantId, userId) }

            exception shouldBe RestaurantNotFoundException()
            verify(exactly = 0) { restaurantRepository.deleteById(restaurantId) }
        }

        it("should throw exception when trying to delete a restaurant with insufficient privileges") {
            every { userService.findOrThrow(any()) } returns User(profile = UserProfile.CUSTOMER)
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)

            val exception = shouldThrow<UserNotAuthorizedException> { restaurantService.delete(restaurantId, userId) }

            exception shouldBe UserNotAuthorizedException()
            verify(exactly = 0) { restaurantRepository.deleteById(restaurant.id) }
        }

        it("should throw exception when trying to delete a restaurant in use") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.deleteById(any()) } throws DataIntegrityViolationException("Violation")

            val exception = shouldThrow<RestaurantInUseException> { restaurantService.delete(restaurantId, userId) }

            exception shouldBe RestaurantInUseException()
            verify(exactly = 1) { restaurantRepository.deleteById(restaurant.id) }
        }
    }

    describe("activate") {
        it("should activate a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.activate(restaurantId, userId)

            restaurant.isActive() shouldBe true
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("inactivate") {
        it("should inactivate a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.inactivate(restaurantId, userId)

            restaurant.isActive() shouldBe false
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("open") {
        it("should open a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.open(restaurantId, userId)

            restaurant.isOpen() shouldBe true
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("close") {
        it("should close a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.close(restaurantId, userId)

            restaurant.isOpen() shouldBe false
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("addPaymentMethod") {
        it("should add a payment method to a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { paymentMethodService.findOrThrow(any()) } returns paymentMethod
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.addPaymentMethod(restaurantId, userId, paymentMethodId)

            restaurant.paymentsMethods shouldContain paymentMethod
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("removePaymentMethod") {
        restaurant.addPaymentMethod(paymentMethod)

        it("should remove a payment method from a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { paymentMethodService.findOrThrow(any()) } returns paymentMethod
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.removePaymentMethod(restaurantId, userId, paymentMethodId)

            restaurant.paymentsMethods shouldNotBe paymentMethod
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("addManager") {
        it("should add a manager to a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.addManager(restaurantId, userId)

            restaurant.managers shouldContain user
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("removeManager") {
        it("should remove a manager from a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { userService.findOrThrow(any()) } returns user
            every { restaurantRepository.save(any()) } returns restaurant

            restaurantService.removeManager(restaurantId, userId)

            restaurant.managers shouldNotBe user
            verify { restaurantRepository.save(restaurant) }
        }
    }

    describe("findAllPaymentMethods") {
        it("should return all payment methods for a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { restaurantRepository.save(any()) } returns restaurant

            val result = restaurantService.findAllPaymentMethods(restaurantId)

            result shouldBe restaurant.paymentsMethods
        }
    }

    describe("findAllManagers") {
        it("should return all managers for a restaurant") {
            every { restaurantRepository.findById(any()) } returns Optional.of(restaurant)
            every { restaurantRepository.save(any()) } returns restaurant

            val result = restaurantService.findAllManagers(restaurantId)

            result shouldBe restaurant.managers
        }
    }
})
