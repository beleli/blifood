package br.com.blifood.domain.listener

import br.com.blifood.domain.createOrder
import br.com.blifood.domain.event.ConfirmedOrderEvent
import br.com.blifood.domain.service.EmailService
import io.kotest.core.spec.style.StringSpec
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class ConfirmedOrderEventListenerTest : StringSpec({

    "notifyConfirmedOrder should send email when called" {
        val emailService = mockk<EmailService>()
        val eventListener = ConfirmedOrderEventListener(emailService)

        val order = createOrder()
        val event = ConfirmedOrderEvent(order)

        every { emailService.send(any()) } just runs

        eventListener.notifyConfirmedOrder(event)

        verify {
            emailService.send(
                match {
                    it.to == setOf(order.user.email) &&
                        it.subject == order.restaurant.name &&
                        it.template == "emails/order-confirmed.html" &&
                        it.variables == mapOf("order" to order)
                }
            )
        }

        confirmVerified(emailService)
    }
})
