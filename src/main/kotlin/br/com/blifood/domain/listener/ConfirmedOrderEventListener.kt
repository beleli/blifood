package br.com.blifood.domain.listener

import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.event.ConfirmedOrderEvent
import br.com.blifood.domain.service.EmailService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ConfirmedOrderEventListener(
    private val emailService: EmailService
) {

    @TransactionalEventListener
    fun notifyConfirmedOrder(event: ConfirmedOrderEvent) {
        val order: Order = event.order
        val message = EmailService.Message(
            setOf(order.user.email),
            order.restaurant.name,
            "emails/order-confirmed.html",
            mapOf(Pair("order", order))
        )
        emailService.send(message)
    }
}
