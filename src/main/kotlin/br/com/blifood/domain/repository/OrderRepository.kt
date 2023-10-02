package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByCode(code: String): Optional<Order>
}
