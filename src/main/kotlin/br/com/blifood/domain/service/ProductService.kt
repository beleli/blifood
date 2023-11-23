package br.com.blifood.domain.service

import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.exception.ProductAlreadyExistsException
import br.com.blifood.domain.exception.ProductInUseException
import br.com.blifood.domain.exception.ProductNotFoundException
import br.com.blifood.domain.repository.ProductRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val restaurantService: RestaurantService
) {

    @Transactional(readOnly = true)
    fun findAll(restaurantId: Long, pageable: Pageable): Page<Product> {
        return productRepository.findByRestaurantId(restaurantId, pageable)
    }

    @Transactional(readOnly = true)
    fun findOrThrow(restaurantId: Long, productId: Long): Product {
        return productRepository.findByIdAndRestaurantId(productId, restaurantId)
            .orElseThrow { ProductNotFoundException() }
    }

    @Transactional
    fun save(product: Product): Product {
        product.restaurant = restaurantService.findOrThrow(product.restaurant.id)
        productRepository.findByNameAndRestaurantId(product.name, product.restaurant.id)?.let {
            if (it.id != product.id) throw ProductAlreadyExistsException()
        }
        return productRepository.save(product)
    }

    @Transactional(rollbackFor = [ProductInUseException::class])
    fun delete(restaurantId: Long, productId: Long) {
        runCatching {
            productRepository.delete(findOrThrow(restaurantId, productId))
            productRepository.flush()
        }.onFailure { // it: Throwable ->
            when (it) {
                is DataIntegrityViolationException -> throw ProductInUseException()
                else -> throw it
            }
        }
    }
}
