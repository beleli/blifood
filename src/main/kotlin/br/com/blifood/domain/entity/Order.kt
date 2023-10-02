package br.com.blifood.domain.entity

import br.com.blifood.domain.event.CanceledOrderEvent
import br.com.blifood.domain.event.ConfirmedOrderEvent
import br.com.blifood.domain.exception.OrderNotCanceledException
import br.com.blifood.domain.exception.OrderNotConfirmedException
import br.com.blifood.domain.exception.OrderNotDeliveredException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.domain.AbstractAggregateRoot
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "tb_order")
data class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", unique = true, nullable = false)
    val id: Long = 0,

    @Column(name = "cd_code", unique = true, nullable = false)
    val code: String = UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "id_restaurant", nullable = false)
    var restaurant: Restaurant = Restaurant(),

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    var user: User = User(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment_method", nullable = false)
    var paymentMethod: PaymentMethod = PaymentMethod(),

    @field:PositiveOrZero
    @Column(name = "vl_subtotal", nullable = false)
    var subtotal: BigDecimal = BigDecimal.ZERO,

    @field:PositiveOrZero
    @Column(name = "vl_delivery_fee", nullable = false)
    var deliveryFee: BigDecimal = BigDecimal.ZERO,

    @field:PositiveOrZero
    @Column(name = "vl_total", nullable = false)
    private var total: BigDecimal = BigDecimal.ZERO,

    @Embedded @Valid
    val deliveryAddress: Address = Address(),

    @Enumerated(EnumType.STRING)
    @Column(name = "cd_status", nullable = false)
    private var status: OrderStatus = OrderStatus.CREATED,

    @CreationTimestamp
    @Column(name = "dt_create", nullable = false)
    private val created: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "dt_confirm")
    private var confirmed: OffsetDateTime? = null,

    @Column(name = "dt_cancel")
    private var canceled: OffsetDateTime? = null,

    @Column(name = "dt_delivery")
    private var delivered: OffsetDateTime? = null,

    @field:Valid
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val items: List<OrderItem> = mutableListOf()
) : AbstractAggregateRoot<Order>() {
    fun status() = status
    fun total() = total
    fun created() = created
    fun confirmed() = confirmed
    fun canceled() = canceled
    fun delivered() = delivered

    fun calculateTotal() {
        items.map { it.calculateTotal() }
        subtotal = items.sumOf { it.total() }
        total = subtotal.plus(deliveryFee)
    }

    fun confirm() = apply {
        if (!status.isChangeableTo(OrderStatus.CONFIRMED)) throw OrderNotConfirmedException()
        status = OrderStatus.CONFIRMED
        confirmed = OffsetDateTime.now()
        registerEvent(ConfirmedOrderEvent(this))
    }

    fun cancel() = apply {
        if (!status.isChangeableTo(OrderStatus.CANCELED)) throw OrderNotCanceledException()
        status = OrderStatus.CANCELED
        canceled = OffsetDateTime.now()
        registerEvent(CanceledOrderEvent(this))
    }

    fun delivery() = apply {
        if (!status.isChangeableTo(OrderStatus.DELIVERED)) throw OrderNotDeliveredException()
        status = OrderStatus.DELIVERED
        delivered = OffsetDateTime.now()
    }
}
