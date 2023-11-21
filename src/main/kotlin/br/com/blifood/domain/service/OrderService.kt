package br.com.blifood.domain.service

import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.exception.OrderInvalidPaymentMethodException
import br.com.blifood.domain.exception.OrderNotFoundException
import br.com.blifood.domain.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val cityService: CityService,
    private val restaurantService: RestaurantService,
    private val paymentMethodService: PaymentMethodService,
    private val productService: ProductService
) {

    @Transactional(readOnly = true)
    fun findOrThrow(code: String): Order {
        return orderRepository.findByCode(code).orElseThrow { OrderNotFoundException() }
    }

    @Transactional
    fun issue(order: Order): Order {
        order.deliveryAddress.city = cityService.findOrThrow(order.deliveryAddress.city.id)
        order.restaurant = restaurantService.findOrThrow(order.restaurant.id)
        order.paymentMethod = paymentMethodService.findOrThrow(order.paymentMethod.id)
        if (!order.restaurant.paymentsMethods.contains(order.paymentMethod)) throw OrderInvalidPaymentMethodException()
        order.items.map {
            it.product = productService.findOrThrow(order.restaurant.id, it.product.id)
            it.unitPrice = it.product.price
            it.order = order
        }
        order.deliveryFee = order.restaurant.deliveryFee
        order.calculateTotal()

        return orderRepository.save(order)
    }

    @Transactional
    fun confirm(code: String) {
        val order = findOrThrow(code)
        orderRepository.save(order.confirm())
    }

    @Transactional
    fun cancel(code: String) {
        val order = findOrThrow(code)
        orderRepository.save(order.cancel())
    }

    @Transactional
    fun delivery(code: String) {
        val order = findOrThrow(code)
        orderRepository.save(order.delivery())
    }
}
