package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Product>
    fun findByIdAndRestaurantId(productId: Long, restaurantId: Long): Optional<Product>
    fun findByNameAndRestaurantId(name: String, restaurantId: Long): Product?
}
