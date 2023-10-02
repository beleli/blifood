package br.com.blifood.domain.event

import br.com.blifood.domain.entity.Order

data class ConfirmedOrderEvent(
    val order: Order
)
