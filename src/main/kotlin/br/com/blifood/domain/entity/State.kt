package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tb_state")
data class State(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_state", unique = true, nullable = false)
    val id: Long = 0,

    @field:Size(min = 2, max = 2)
    @Column(name = "ds_name", nullable = false)
    var name: String = ""
)
