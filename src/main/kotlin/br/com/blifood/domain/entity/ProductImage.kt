package br.com.blifood.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tb_product_image")
data class ProductImage(

    @Id
    @Column(name = "id_product", unique = true, nullable = false)
    val id: Long = 0,

    @field:Size(min = 1, max = 150)
    @Column(name = "ds_file_name", nullable = false)
    val fileName: String = "",

    @field:Size(max = 150)
    @Column(name = "ds_description")
    val description: String? = null,

    @field:Size(min = 1, max = 80)
    @Column(name = "ds_content_type", nullable = false)
    val contentType: String = "",

    @field:PositiveOrZero
    @Column(name = "vl_size", nullable = false)
    val size: Long = 0
)
