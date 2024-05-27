package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.Size

@Embeddable
data class Address(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address_city", nullable = false)
    var city: City = City(),

    @field:Size(min = 8, max = 8)
    @Column(name = "ds_address_zip_code", nullable = false)
    val zipCode: String = "",

    @field:Size(min = 1, max = 100)
    @Column(name = "ds_address_street", nullable = false)
    val street: String = "",

    @field:Size(min = 1, max = 20)
    @Column(name = "ds_address_number", nullable = false)
    val number: String = "",

    @field:Size(min = 1, max = 60)
    @Column(name = "ds_address_complement")
    val complement: String? = null,

    @field:Size(min = 1, max = 60)
    @Column(name = "ds_address_district", nullable = false)
    val district: String = ""
)
