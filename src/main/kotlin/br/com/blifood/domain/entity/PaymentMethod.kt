package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tb_payment_method")
data class PaymentMethod(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment_method", unique = true, nullable = false)
    val id: Long = 0,

    @field:Size(min = 1, max = 60)
    @Column(name = "ds_description", nullable = false)
    var description: String = ""
)
