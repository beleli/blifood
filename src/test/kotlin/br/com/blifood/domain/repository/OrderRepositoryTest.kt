package br.com.blifood.domain.repository

import br.com.blifood.domain.createAddress
import br.com.blifood.domain.createCity
import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.createOrder
import br.com.blifood.domain.createPaymentMethod
import br.com.blifood.domain.createProduct
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.createState
import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.entity.OrderStatus
import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.State
import br.com.blifood.domain.entity.User
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
class OrderRepositoryTest {

    @Autowired
    private lateinit var cityRepository: CityRepository

    @Autowired
    private lateinit var stateRepository: StateRepository

    @Autowired
    private lateinit var culinaryRepository: CulinaryRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var paymentMethodRepository: PaymentMethodRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    private var savedCulinary: Culinary = createCulinary()
    private var savedState: State = createState()
    private var savedCity: City = createCity()
    private var savedRestaurant: Restaurant = createRestaurant()
    private var savedPaymentMethod: PaymentMethod = createPaymentMethod()
    private var savedProduct: Product = createProduct()
    private var savedUser: User = createUser()
    private var savedOrder: Order = createOrder()

    @BeforeEach
    fun setUp() {
        savedState = stateRepository.save(createState())
        savedCity = cityRepository.save(createCity(state = savedState))
        savedCulinary = culinaryRepository.save(createCulinary())
        savedPaymentMethod = paymentMethodRepository.save(createPaymentMethod())
        savedRestaurant = restaurantRepository.save(createRestaurant(culinary = savedCulinary, address = createAddress(city = savedCity)).addPaymentMethod(savedPaymentMethod))
        savedProduct = productRepository.save(createProduct(restaurant = savedRestaurant))
        savedUser = userRepository.save(createUser())
        savedOrder = orderRepository.save(createOrder(user = savedUser, restaurant = savedRestaurant, paymentMethod = savedPaymentMethod, items = emptyList(), deliveryAddress = createAddress(city = savedCity)))
    }

    @Test
    fun `should insert a new order`() {
        val result = orderRepository.findById(savedOrder.id)
        assertTrue(result.isPresent)
        assertEquals(savedOrder, result.get())
    }

    @Test
    fun `should update an existing order`() {
        savedOrder.cancel()

        val updatedOrder = orderRepository.save(savedOrder)

        val result = orderRepository.findById(updatedOrder.id)
        assertTrue(result.isPresent)
        assertEquals(OrderStatus.CANCELED, result.get().status())
    }

    @Test
    fun `should delete an existing order`() {
        orderRepository.delete(savedOrder)
        val result = orderRepository.findById(savedOrder.id)
        assertTrue(result.isEmpty)
    }
}
