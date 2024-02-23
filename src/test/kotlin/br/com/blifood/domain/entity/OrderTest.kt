package br.com.blifood.domain.entity

import br.com.blifood.domain.createOrder
import br.com.blifood.domain.exception.OrderNotCanceledException
import br.com.blifood.domain.exception.OrderNotConfirmedException
import br.com.blifood.domain.exception.OrderNotDeliveredException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.math.BigDecimal

class OrderTest : StringSpec({

    "New order should have status CREATED" {
        val order = createOrder()

        order.status() shouldBe OrderStatus.CREATED
        order.created() shouldNotBe null
    }

    "Order when confirmed, it should have status CONFIRMED" {
        val order = createOrder()

        order.confirm()

        order.status() shouldBe OrderStatus.CONFIRMED
        order.confirmed() shouldNotBe null
    }

    "Order when canceled, it should have status CANCELED" {
        val order = createOrder()

        order.cancel()

        order.status() shouldBe OrderStatus.CANCELED
        order.canceled() shouldNotBe null
    }

    "Order with status CONFIRMED, when canceled, it should have status CANCELED" {
        val order = createOrder()

        order.confirm()
        order.cancel()

        order.status() shouldBe OrderStatus.CANCELED
        order.confirmed() shouldNotBe null
        order.canceled() shouldNotBe null
    }

    "Order with status CONFIRMED, when delivered, it should have status DELIVERED" {
        val order = createOrder()

        order.confirm()
        order.delivery()

        order.status() shouldBe OrderStatus.DELIVERED
        order.confirmed() shouldNotBe null
        order.delivered() shouldNotBe null
    }

    "Order with status CANCELED, when trying to confirm, it should throw OrderNotConfirmedException" {
        val order = createOrder()
        order.cancel()

        val exception = shouldThrow<OrderNotConfirmedException> { order.confirm() }

        exception shouldBe OrderNotConfirmedException()
    }

    "Order with status CANCELED, when trying to delivered, it should throw OrderNotDeliveredException" {
        val order = createOrder()
        order.cancel()

        val exception = shouldThrow<OrderNotDeliveredException> { order.delivery() }

        exception shouldBe OrderNotDeliveredException()
    }

    "Order when delivered, it should have status DELIVERED" {
        val order = createOrder()

        order.confirm()
        order.delivery()

        order.status() shouldBe OrderStatus.DELIVERED
        order.confirmed() shouldNotBe null
        order.delivered() shouldNotBe null
    }

    "Order with status DELIVERED, when trying to cancel, it should throw OrderNotCanceledException" {
        val order = createOrder()

        order.confirm()
        order.delivery()

        val exception = shouldThrow<OrderNotCanceledException> { order.cancel() }

        exception shouldBe OrderNotCanceledException()
    }

    "Order should calculate total" {
        val order = createOrder()

        order.calculateTotal()
        val total = order.total()

        total shouldBe BigDecimal.valueOf(10.0)
    }
})
