package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

@Entity
@Table(name = "tb_product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product", unique = true, nullable = false)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "id_restaurant", nullable = false)
    var restaurant: Restaurant = Restaurant(),

    @field:Size(min = 1, max = 80)
    @Column(name = "ds_name", nullable = false)
    var name: String = "",

    @field:NotBlank
    @Column(name = "ds_description", nullable = false)
    var description: String = "",

    @field:PositiveOrZero
    @Column(name = "vl_price", nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(name = "fl_active", nullable = false)
    var active: Boolean = true
)
