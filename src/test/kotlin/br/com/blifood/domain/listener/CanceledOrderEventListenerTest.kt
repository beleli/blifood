package br.com.blifood.domain.listener

import br.com.blifood.domain.createOrder
import br.com.blifood.domain.event.CanceledOrderEvent
import br.com.blifood.domain.service.EmailService
import io.kotest.core.spec.style.StringSpec
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class CanceledOrderEventListenerTest : StringSpec({

    "notifyCanceledOrder should send email when called" {
        val emailService = mockk<EmailService>()
        val eventListener = CanceledOrderEventListener(emailService)

        val order = createOrder()
        val event = CanceledOrderEvent(order)

        every { emailService.send(any()) } just runs

        eventListener.notifyCanceledOrder(event)

        verify {
            emailService.send(
                match {
                    it.to == setOf(order.user.email) &&
                        it.subject == order.restaurant.name &&
                        it.template == "emails/order-canceled.html" &&
                        it.variables == mapOf("order" to order)
                }
            )
        }

        confirmVerified(emailService)
    }
})
