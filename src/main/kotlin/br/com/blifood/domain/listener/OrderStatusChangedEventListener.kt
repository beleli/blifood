package br.com.blifood.domain.listener

import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.event.OrderStatusChangedEvent
import br.com.blifood.domain.service.EmailService
import br.com.blifood.domain.service.MetricService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class OrderStatusChangedEventListener(
    private val metricService: MetricService,
    private val emailService: EmailService
) {

    @Async
    @EventListener
    fun notifyOrderStatusChanged(event: OrderStatusChangedEvent) {
        val order: Order = event.order
        metricService.sendOrderStatusChanged(order.status())
        order.status().emailTemplate?.let {
            emailService.send(
                EmailService.Message(
                    setOf(order.user.email),
                    order.restaurant.name,
                    it,
                    mapOf(Pair("order", order))
                )
            )
        }
    }
}
