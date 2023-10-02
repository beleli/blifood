package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "tb_order_item")
data class OrderItem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_item", unique = true, nullable = false)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    var order: Order = Order(),

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    var product: Product = Product(),

    @Column(name = "vl_amount", nullable = false)
    val amount: Int = 0,

    @Column(name = "vl_unit_price", nullable = false)
    var unitPrice: BigDecimal = BigDecimal.ZERO,

    @Column(name = "vl_total", nullable = false)
    private var total: BigDecimal = BigDecimal.ZERO,

    @Column(name = "ds_observation")
    val observation: String?
) {
    fun total() = total
    fun calculateTotal() {
        total = unitPrice.times(amount.toBigDecimal())
    }
}
