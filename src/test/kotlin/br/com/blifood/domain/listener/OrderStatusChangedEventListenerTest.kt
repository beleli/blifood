package br.com.blifood.domain.listener

import br.com.blifood.domain.createOrder
import br.com.blifood.domain.event.OrderStatusChangedEvent
import br.com.blifood.domain.service.EmailService
import br.com.blifood.domain.service.MetricService
import io.kotest.core.spec.style.StringSpec
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class OrderStatusChangedEventListenerTest : StringSpec({

    val metricService = mockk<MetricService>()
    val emailService = mockk<EmailService>()
    val eventListener = OrderStatusChangedEventListener(metricService, emailService)

    beforeTest {
        clearMocks(metricService, emailService)
    }

    "notifyOrderStatusChanged should send metric and email when called" {
        val order = createOrder().confirm()
        val event = OrderStatusChangedEvent(order)

        every { metricService.sendOrderStatusChanged(any()) } just runs
        every { emailService.send(any()) } just runs

        eventListener.notifyOrderStatusChanged(event)

        verify { metricService.sendOrderStatusChanged(order.status()) }
        verify {
            emailService.send(
                match {
                    it.to == setOf(order.user.email) &&
                        it.subject == order.restaurant.name &&
                        it.template == order.status().emailTemplate &&
                        it.variables == mapOf("order" to order)
                }
            )
        }

        confirmVerified(emailService)
    }

    "notifyOrderStatusChanged should send only metric when called" {
        val order = createOrder()
        val event = OrderStatusChangedEvent(order)

        every { metricService.sendOrderStatusChanged(any()) } just runs

        eventListener.notifyOrderStatusChanged(event)

        verify { metricService.sendOrderStatusChanged(order.status()) }
        verify(exactly = 0) { emailService.send(any()) }

        confirmVerified(emailService)
    }
})
