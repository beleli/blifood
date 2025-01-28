package br.com.blifood.domain.repository

import br.com.blifood.domain.createAddress
import br.com.blifood.domain.createCity
import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.createProduct
import br.com.blifood.domain.createProductImage
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.createState
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.entity.ProductImage
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataJpaTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductImageRepositoryTest {

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

    @Autowired
    private lateinit var productImageRepository: ProductImageRepository

    private var savedCulinary: Culinary = createCulinary()
    private var savedState: State = createState()
    private var savedCity: City = createCity()
    private var savedRestaurant: Restaurant = createRestaurant()
    private var savedProduct: Product = createProduct()
    private lateinit var savedProductImage: ProductImage

    @BeforeEach
    fun setUp() {
        savedState = stateRepository.save(createState())
        savedCity = cityRepository.save(createCity(state = savedState))
        savedCulinary = culinaryRepository.save(createCulinary())
        savedRestaurant = restaurantRepository.save(createRestaurant(culinary = savedCulinary, address = createAddress(city = savedCity)))
        savedProduct = productRepository.save(createProduct(restaurant = savedRestaurant))
        savedProductImage = productImageRepository.save(createProductImage(id = savedProduct.id))
    }

    @Test
    fun `should insert a new product image`() {
        val result = productImageRepository.findById(savedProductImage.id)
        assertTrue(result.isPresent)
        assertEquals(savedProductImage, result.get())
    }

    @Test
    fun `should delete an existing product image`() {
        productImageRepository.delete(savedProductImage)
        val result = productImageRepository.findById(savedProductImage.id)
        assertTrue(result.isEmpty)
    }
}
