package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.PaymentMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
    fun findByDescription(description: String): PaymentMethod?
}
