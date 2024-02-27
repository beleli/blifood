package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.OrderController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.input.AddressInputModel
import br.com.blifood.api.v1.model.input.OrderInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createOrder
import br.com.blifood.domain.entity.Address
import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.service.OrderService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import org.springframework.http.ResponseEntity

class OrderControllerTest : StringSpec({
    val orderService = mockk<OrderService>()
    val controller = OrderController(orderService)

    val order = createOrder()
    val orderCode = order.code

    fun createInputAddress(address: Address) = AddressInputModel(
        cityId = address.city.id,
        zipCode = address.zipCode,
        street = address.street,
        number = address.number,
        complement = address.complement,
        district = address.district
    )

    fun createOrderModel(order: Order) = OrderInputModel(
        restaurantId = order.restaurant.id,
        paymentMethodId = order.paymentMethod.id,
        deliveryAddress = createInputAddress(order.deliveryAddress),
        items = emptyList()
    )

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findByCode should return OrderModel" {
        every { orderService.findOrThrow(orderCode) } returns order

        val result = controller.findByCode(orderCode)
        result shouldBe order.toModel()
    }

    "create should return created OrderModel" {
        every { orderService.issue(any()) } returns order

        val result = controller.create(createOrderModel(order))
        result shouldBe order.toModel()
    }

    "confirm should return NO_CONTENT" {
        every { orderService.confirm(any()) } answers {}

        val result = controller.confirm(orderCode)
        result shouldBe ResponseEntity.noContent().build()
    }

    "delivery should return NO_CONTENT" {
        every { orderService.delivery(any()) } answers {}

        val result = controller.delivery(orderCode)
        result shouldBe ResponseEntity.noContent().build()
    }

    "cancel should return NO_CONTENT" {
        every { orderService.cancel(any()) } answers {}

        val result = controller.cancel(orderCode)
        result shouldBe ResponseEntity.noContent().build()
    }
})
