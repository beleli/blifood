package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.RestaurantController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.input.AddressInputModel
import br.com.blifood.api.v1.model.input.RestaurantInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.Address
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.StateNotFoundException
import br.com.blifood.domain.service.RestaurantService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel

class RestaurantControllerTest : StringSpec({
    val restaurantService = mockk<RestaurantService>()
    val restaurantController = RestaurantController(restaurantService)

    val restaurant: Restaurant = createRestaurant()
    val restaurantId = restaurant.id
    val user: User = createUser()
    val userId = user.id

    fun createInputAddress(address: Address) = AddressInputModel(
        cityId = address.city.id,
        zipCode = address.zipCode,
        street = address.street,
        number = address.number,
        complement = address.complement,
        district = address.district
    )

    fun createInputModel(restaurant: Restaurant) = RestaurantInputModel(
        name = restaurant.name,
        deliveryFee = restaurant.deliveryFee,
        culinaryId = restaurant.culinary.id,
        address = createInputAddress(restaurant.address)
    )

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return PagedModel of RestaurantModel" {
        val pageable: Pageable = mockk()
        every { restaurantService.findAll(any()) } returns PageImpl(listOf(restaurant))

        val result: PagedModel<RestaurantModel> = restaurantController.findAll(pageable)
        result.content.size shouldBe 1
    }

    "findById should return RestaurantModel" {
        every { restaurantService.findOrThrow(any()) } returns restaurant

        val result: RestaurantModel = restaurantController.findById(restaurantId)
        result shouldBe restaurant.toModel()
    }

    "create should return created RestaurantModel" {
        every { restaurantService.save(any(), any()) } returns restaurant

        val result: RestaurantModel = restaurantController.create(createInputModel(restaurant))
        result shouldBe restaurant.toModel()
    }

    "create should throw BusinessException" {
        every { restaurantService.save(any(), any()) } throws StateNotFoundException()

        val exception = shouldThrow<BusinessException> { restaurantController.create(createInputModel(restaurant)) }
        exception shouldBe BusinessException(StateNotFoundException().message)
    }

    "create should throw Exception" {
        every { restaurantService.save(any(), any()) } throws Throwable()

        val exception = shouldThrow<Throwable> { restaurantController.create(createInputModel(restaurant)) }
        exception shouldBe Throwable()
    }

    "alter should return altered RestaurantModel" {
        every { restaurantService.findOrThrow(any()) } returns restaurant
        every { restaurantService.save(any(), any()) } returns restaurant

        val result: RestaurantModel = restaurantController.alter(restaurantId, createInputModel(restaurant))
        result shouldBe restaurant.toModel()
    }

    "delete should return NO_CONTENT" {
        every { restaurantService.delete(any(), any()) } just runs

        restaurantController.delete(restaurantId)
        verify { restaurantService.delete(restaurantId, userId) }
    }

    "active should return NO_CONTENT" {
        every { restaurantService.activate(any(), any()) } just runs

        restaurantController.activate(restaurantId)
        verify { restaurantService.activate(restaurantId, userId) }
    }

    "inactivate should return NO_CONTENT" {
        every { restaurantService.inactivate(any(), any()) } just runs

        restaurantController.inactivate(restaurantId)
        verify { restaurantService.inactivate(restaurantId, userId) }
    }

    "open should return NO_CONTENT" {
        every { restaurantService.open(any(), any()) } just runs

        restaurantController.open(restaurantId)
        verify { restaurantService.open(restaurantId, userId) }
    }

    "close should return NO_CONTENT" {
        every { restaurantService.close(any(), any()) } just runs

        restaurantController.close(restaurantId)
        verify { restaurantService.close(restaurantId, userId) }
    }
})
