package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "tb_user")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", unique = true, nullable = false)
    val id: Long = 0,

    @field:Size(min = 1, max = 80)
    @Column(name = "ds_name", nullable = false)
    var name: String = "",

    @field:Size(min = 1, max = 255)
    @Column(name = "ds_email", nullable = false)
    var email: String = "",

    @field:Size(min = 1, max = 255)
    @Column(name = "ds_password", nullable = false)
    var password: String = "",

    @CreationTimestamp
    @Column(name = "dt_create", nullable = false)
    val create: OffsetDateTime? = null
)
