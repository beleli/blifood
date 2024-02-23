package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "tb_restaurant")
data class Restaurant(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restaurant", unique = true, nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_culinary", nullable = false)
    var culinary: Culinary = Culinary(),

    @Embedded @Valid
    var address: Address = Address(),

    @field:Size(min = 1, max = 80)
    @Column(name = "ds_name", nullable = false)
    var name: String = "",

    @field:PositiveOrZero
    @Column(name = "vl_delivery_fee", nullable = false)
    var deliveryFee: BigDecimal = BigDecimal.ZERO,

    @Column(name = "fl_active", nullable = false)
    private var active: Boolean = true,

    @Column(name = "fl_open", nullable = false)
    private var open: Boolean = false,

    @CreationTimestamp
    @Column(name = "dt_create", nullable = false)
    val create: OffsetDateTime = OffsetDateTime.now(),

    @UpdateTimestamp
    @Column(name = "dt_update")
    val update: OffsetDateTime? = null,

    @ManyToMany
    @JoinTable(
        name = "tb_restaurant_payment_method",
        joinColumns = [JoinColumn(name = "id_restaurant")],
        inverseJoinColumns = [JoinColumn(name = "id_payment_method")]
    )
    var paymentsMethods: MutableSet<PaymentMethod> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "tb_restaurant_user",
        joinColumns = [JoinColumn(name = "id_restaurant")],
        inverseJoinColumns = [JoinColumn(name = "id_user")]
    )
    var managers: MutableSet<User> = mutableSetOf(),

    @OneToMany(mappedBy = "restaurant")
    val products: MutableSet<Product> = mutableSetOf()
) {
    fun isActive() = active
    fun activate() = apply { active = true }
    fun inactivate() = apply { active = false }
    fun isOpen() = open
    fun open() = apply { open = true }
    fun close() = apply { open = false }
    fun addPaymentMethod(paymentMethod: PaymentMethod) = apply { paymentsMethods.add(paymentMethod) }
    fun removePaymentMethod(paymentMethod: PaymentMethod) = apply { paymentsMethods.remove(paymentMethod) }
    fun addManager(user: User) = apply { managers.add(user) }
    fun removeManager(user: User) = apply { managers.remove(user) }
}
