package br.com.blifood.domain.repository

import br.com.blifood.domain.createAddress
import br.com.blifood.domain.createCity
import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.createProduct
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.createState
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@DataJpaTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private lateinit var cityRepository: CityRepository

    @Autowired
    private lateinit var stateRepository: StateRepository

    @Autowired
    private lateinit var culinaryRepository: CulinaryRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var savedCulinary: Culinary = createCulinary()
    private var savedState: State = createState()
    private var savedCity: City = createCity()
    private var savedRestaurant: Restaurant = createRestaurant()
    private var savedProduct: Product = createProduct()

    @BeforeEach
    fun setUp() {
        savedState = stateRepository.save(createState())
        savedCity = cityRepository.save(createCity(state = savedState))
        savedCulinary = culinaryRepository.save(createCulinary())
        savedRestaurant = restaurantRepository.save(createRestaurant(culinary = savedCulinary, address = createAddress(city = savedCity)))
        savedProduct = productRepository.save(createProduct(restaurant = savedRestaurant))
    }

    @Test
    fun `should find product by restaurant id`() {
        val pageable = PageRequest.of(0, 10)
        val result = productRepository.findByRestaurantId(savedProduct.restaurant.id, pageable)
        assertEquals(1, result.totalElements)
        assertEquals(savedProduct, result.content[0])
    }

    @Test
    fun `should find product by id and restaurant id`() {
        val result = productRepository.findByIdAndRestaurantId(savedProduct.id, savedProduct.restaurant.id)
        assertEquals(savedProduct, result.get())
    }

    @Test
    fun `should return null if product not found by id and restaurant id`() {
        val result = productRepository.findByIdAndRestaurantId(999L, savedProduct.restaurant.id)
        assertEquals(Optional.empty<Product>(), result)
    }

    @Test
    fun `should find product by name and restaurant id`() {
        val result = productRepository.findByNameAndRestaurantId(savedProduct.name, savedProduct.restaurant.id)
        assertEquals(savedProduct, result)
    }

    @Test
    fun `should return null if product not found by name and restaurant id`() {
        val result = productRepository.findByNameAndRestaurantId("Unknown Product", savedProduct.restaurant.id)
        assertNull(result)
    }

    @Test
    fun `should insert a new product`() {
        val result = productRepository.findById(savedProduct.id)
        assertEquals(savedProduct, result.get())
    }

    @Test
    fun `should update an existing product`() {
        savedProduct.name = "Updated Product"

        val updatedProduct = productRepository.save(savedProduct)

        val result = productRepository.findById(updatedProduct.id)
        assertEquals("Updated Product", result.get().name)
    }

    @Test
    fun `should delete an existing product`() {
        productRepository.delete(savedProduct)
        val result = productRepository.findById(savedProduct.id)
        assertEquals(result.isEmpty, true)
    }
}
