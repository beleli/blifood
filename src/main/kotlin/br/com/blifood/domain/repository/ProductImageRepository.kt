package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepository : JpaRepository<ProductImage, Long>
