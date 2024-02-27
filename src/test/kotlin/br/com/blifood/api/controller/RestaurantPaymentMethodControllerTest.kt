package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.RestaurantPaymentMethodController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.domain.createPaymentMethod
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

class RestaurantPaymentMethodControllerTest : StringSpec({
    val restaurantService = mockk<RestaurantService>()
    val controller = RestaurantPaymentMethodController(restaurantService)

    val restaurantId = 1L
    val paymentMethodId = 1L

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return CollectionModel of PaymentMethodModel" {
        val paymentMethods = setOf(createPaymentMethod())
        every { restaurantService.findAllPaymentMethods(restaurantId) } returns paymentMethods

        val result: CollectionModel<PaymentMethodModel> = controller.findAll(restaurantId)
        result.content.size shouldBe 1
    }

    "add should return NO_CONTENT" {
        every { restaurantService.addPaymentMethod(any(), any(), any()) } answers {}

        val result: ResponseEntity<Void> = controller.add(restaurantId, paymentMethodId)
        result.statusCode shouldBe HttpStatus.NO_CONTENT
    }

    "remove should return NO_CONTENT" {
        every { restaurantService.removePaymentMethod(any(), any(), any()) } answers {}

        val result: ResponseEntity<Void> = controller.remove(restaurantId, paymentMethodId)
        result.statusCode shouldBe HttpStatus.NO_CONTENT
    }
})
