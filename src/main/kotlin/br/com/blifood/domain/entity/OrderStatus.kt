package br.com.blifood.domain.entity

enum class OrderStatus(private val previousStatuses: Set<OrderStatus>) {
    CREATED(emptySet()),
    CONFIRMED(setOf(CREATED)),
    DELIVERED(setOf(CONFIRMED)),
    CANCELED(setOf(CREATED, CONFIRMED));

    fun isChangeableTo(status: OrderStatus) = this in status.previousStatuses
}
