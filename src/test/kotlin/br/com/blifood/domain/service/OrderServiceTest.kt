package br.com.blifood.domain.service

import br.com.blifood.domain.createOrder
import br.com.blifood.domain.createPaymentMethod
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.entity.OrderStatus
import br.com.blifood.domain.exception.OrderInvalidPaymentMethodException
import br.com.blifood.domain.exception.OrderNotCanceledException
import br.com.blifood.domain.exception.OrderNotConfirmedException
import br.com.blifood.domain.exception.OrderNotDeliveredException
import br.com.blifood.domain.exception.OrderNotFoundException
import br.com.blifood.domain.repository.OrderRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.util.Optional
import java.util.UUID

class OrderServiceTest : DescribeSpec({
    val orderRepository = mockk<OrderRepository>()
    val cityService = mockk<CityService>()
    val restaurantService = mockk<RestaurantService>()
    val paymentMethodService = mockk<PaymentMethodService>()
    val productService = mockk<ProductService>()

    val orderService = OrderService(orderRepository, cityService, restaurantService, paymentMethodService, productService)

    val restaurant = createRestaurant().addPaymentMethod(createPaymentMethod())
    val paymentMethod = restaurant.paymentsMethods.first()
    val orderCode = UUID.randomUUID().toString()

    describe("issue") {
        it("should calculate total and save the order") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod)
            val product = order.items.first().product
            val city = order.deliveryAddress.city

            every { restaurantService.findOrThrow(any()) } returns restaurant
            every { paymentMethodService.findOrThrow(any()) } returns paymentMethod
            every { productService.findOrThrow(any(), any()) } returns product
            every { cityService.findOrThrow(any()) } returns city
            every { orderRepository.save(any()) } returns order

            val result = orderService.issue(order)

            result shouldBe order
            result.subtotal shouldBe BigDecimal.valueOf(10.0)
            result.total() shouldBe BigDecimal.valueOf(20.0)
        }

        it("should throw OrderInvalidPaymentMethodException when restaurant don't have Payment Method") {
            val newRestaurant = createRestaurant()
            val order = createOrder(restaurant = newRestaurant, paymentMethod = paymentMethod)
            val product = order.items.first().product
            val city = order.deliveryAddress.city

            every { restaurantService.findOrThrow(any()) } returns newRestaurant
            every { paymentMethodService.findOrThrow(any()) } returns paymentMethod
            every { productService.findOrThrow(any(), any()) } returns product
            every { cityService.findOrThrow(any()) } returns city

            val exception = shouldThrow<OrderInvalidPaymentMethodException> { orderService.issue(order) }

            exception shouldBe OrderInvalidPaymentMethodException()
        }
    }

    describe("confirm") {
        it("should throw OrderNotFoundException when order is not found") {
            every { orderRepository.findByCode(any()) } returns Optional.empty()

            val exception = shouldThrow<OrderNotFoundException> { orderService.confirm(orderCode) }

            exception shouldBe OrderNotFoundException()
        }

        it("should confirm the order") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod)
            every { orderRepository.findByCode(any()) } returns Optional.of(order)
            every { orderRepository.save(any()) } returns order

            orderService.confirm(order.code)
            order.status() shouldBe OrderStatus.CONFIRMED
        }

        it("should throw OrderNotConfirmedException when order is already confirmed") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { confirm() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotConfirmedException> { orderService.confirm(order.code) }

            exception shouldBe OrderNotConfirmedException()
        }

        it("should throw OrderNotConfirmedException when order is cancelled") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { cancel() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotConfirmedException> { orderService.confirm(order.code) }

            exception shouldBe OrderNotConfirmedException()
        }

        it("should throw OrderNotConfirmedException when order is delivered") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { confirm(); delivery() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotConfirmedException> { orderService.confirm(order.code) }

            exception shouldBe OrderNotConfirmedException()
        }
    }

    describe("cancel") {
        it("should throw OrderNotFoundException when order is not found") {
            every { orderRepository.findByCode(any()) } returns Optional.empty()

            val exception = shouldThrow<OrderNotFoundException> { orderService.cancel(orderCode) }

            exception shouldBe OrderNotFoundException()
        }

        it("should cancel the order") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod)
            every { orderRepository.findByCode(any()) } returns Optional.of(order)
            every { orderRepository.save(any()) } returns order

            orderService.cancel(order.code)
            order.status() shouldBe OrderStatus.CANCELED
        }

        it("should throw OrderNotCanceledException when order is already cancelled") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { cancel() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotCanceledException> { orderService.cancel(order.code) }

            exception shouldBe OrderNotCanceledException()
        }

        it("should cancel the order when order is confirmed") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { confirm() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            orderService.cancel(order.code)
            order.status() shouldBe OrderStatus.CANCELED
        }

        it("should throw OrderNotCanceledException when order is delivered") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { confirm(); delivery() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotCanceledException> { orderService.cancel(order.code) }

            exception shouldBe OrderNotCanceledException()
        }
    }

    describe("delivery") {
        it("should throw OrderNotFoundException when order is not found") {
            every { orderRepository.findByCode(any()) } returns Optional.empty()

            val exception = shouldThrow<OrderNotFoundException> { orderService.delivery(orderCode) }

            exception shouldBe OrderNotFoundException()
        }

        it("should delivery the order") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { confirm() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)
            every { orderRepository.save(any()) } returns order

            orderService.delivery(order.code)
            order.status() shouldBe OrderStatus.DELIVERED
        }

        it("should throw OrderNotDeliveredException when order is already delivery") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { confirm(); delivery() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotDeliveredException> { orderService.delivery(order.code) }

            exception shouldBe OrderNotDeliveredException()
        }

        it("should throw OrderNotDeliveredException when order is created") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod)
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotDeliveredException> { orderService.delivery(order.code) }

            exception shouldBe OrderNotDeliveredException()
        }

        it("should throw OrderNotDeliveredException when order is cancelled") {
            val order = createOrder(restaurant = restaurant, paymentMethod = paymentMethod).apply { cancel() }
            every { orderRepository.findByCode(any()) } returns Optional.of(order)

            val exception = shouldThrow<OrderNotDeliveredException> { orderService.delivery(order.code) }

            exception shouldBe OrderNotDeliveredException()
        }
    }
})
