package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository

interface ProductImageRepository : JpaRepository<ProductImage, Long>
