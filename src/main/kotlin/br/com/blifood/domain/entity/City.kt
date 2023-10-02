package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tb_city")
data class City(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city", unique = true, nullable = false)
    val id: Long = 0,

    @field:Size(min = 1, max = 80)
    @Column(name = "ds_name", nullable = false)
    var name: String = "",

    @field:NotNull
    @field:Valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_state", nullable = false)
    var state: State = State()
)
