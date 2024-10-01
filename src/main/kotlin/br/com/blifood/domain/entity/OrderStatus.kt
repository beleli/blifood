package br.com.blifood.domain.entity

enum class OrderStatus(private val previousStatuses: Set<OrderStatus>, val emailTemplate: String? = null) {
    CREATED(emptySet()),
    CONFIRMED(setOf(CREATED), "emails/order-confirmed.html"),
    DELIVERED(setOf(CONFIRMED)),
    CANCELED(setOf(CREATED, CONFIRMED), "emails/order-canceled.html");

    fun isChangeableTo(status: OrderStatus) = this in status.previousStatuses
}
