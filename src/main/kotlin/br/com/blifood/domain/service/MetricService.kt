package br.com.blifood.domain.service

import br.com.blifood.domain.entity.OrderStatus

interface MetricService {
    fun sendOrderStatusChanged(status: OrderStatus)
}
