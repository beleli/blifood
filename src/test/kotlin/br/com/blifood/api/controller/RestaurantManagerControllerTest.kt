package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.RestaurantManagerController
import br.com.blifood.api.v1.getRequestContextHolderUserId
import br.com.blifood.api.v1.model.UserModel
import br.com.blifood.domain.createUser
import br.com.blifood.domain.service.RestaurantService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class RestaurantManagerControllerTest : StringSpec({
    val restaurantService = mockk<RestaurantService>()
    val controller = RestaurantManagerController(restaurantService)

    val restaurantId = 1L
    val userId = 1L

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getRequestContextHolderUserId() } returns 1L

    "findAll should return CollectionModel of ManagerModel" {
        val managers = setOf(createUser())
        every { restaurantService.findAllManagers(restaurantId) } returns managers

        val result: CollectionModel<UserModel> = controller.findAll(restaurantId)
        result.content.size shouldBe 1
    }

    "add should return NO_CONTENT" {
        every { restaurantService.addManager(any(), any()) } answers {}

        val result: ResponseEntity<Unit> = controller.add(restaurantId, userId)
        result.statusCode shouldBe HttpStatus.NO_CONTENT
    }

    "remove should return NO_CONTENT" {
        every { restaurantService.removeManager(any(), any()) } answers {}

        val result: ResponseEntity<Unit> = controller.remove(restaurantId, userId)
        result.statusCode shouldBe HttpStatus.NO_CONTENT
    }
})
