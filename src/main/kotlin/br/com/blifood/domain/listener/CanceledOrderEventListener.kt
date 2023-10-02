package br.com.blifood.domain.listener

import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.event.CanceledOrderEvent
import br.com.blifood.domain.service.EmailService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CanceledOrderEventListener(
    private val emailService: EmailService
) {

    @TransactionalEventListener
    fun notifyCanceledOrder(event: CanceledOrderEvent) {
        val order: Order = event.order
        val message = EmailService.Message(
            setOf(""),
            order.restaurant.name,
            "emails/order-canceled.html",
            mapOf(Pair("order", order))
        )
        emailService.send(message)
    }
}
