package br.com.blifood.domain.repository

import br.com.blifood.domain.createAddress
import br.com.blifood.domain.createCity
import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.createRestaurant
import br.com.blifood.domain.createState
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.Culinary
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
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataJpaTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestaurantRepositoryTest {

    @Autowired
    private lateinit var cityRepository: CityRepository

    @Autowired
    private lateinit var stateRepository: StateRepository

    @Autowired
    private lateinit var culinaryRepository: CulinaryRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    private var savedCulinary: Culinary = createCulinary()
    private var savedState: State = createState()
    private var savedCity: City = createCity()
    private var savedRestaurant: Restaurant = createRestaurant()

    @BeforeEach
    fun setUp() {
        savedState = stateRepository.save(createState())
        savedCity = cityRepository.save(createCity(state = savedState))
        savedCulinary = culinaryRepository.save(createCulinary())
        savedRestaurant = restaurantRepository.save(createRestaurant(culinary = savedCulinary, address = createAddress(city = savedCity)))
    }

    @Test
    fun `should find restaurant by name and city id`() {
        val result = restaurantRepository.findByNameAndAddressCityId(savedRestaurant.name, savedRestaurant.address.city.id)
        assertEquals(savedRestaurant, result)
    }

    @Test
    fun `should return null if restaurant not found by name and city id`() {
        val result = restaurantRepository.findByNameAndAddressCityId("Unknown Restaurant", 999L)
        assertNull(result)
    }

    @Test
    fun `should insert a new restaurant`() {
        val result = restaurantRepository.findById(savedRestaurant.id)
        assertEquals(savedRestaurant, result.get())
    }

    @Test
    fun `should update an existing restaurant`() {
        savedRestaurant.name = "Updated Restaurant"

        val updatedRestaurant = restaurantRepository.save(savedRestaurant)

        val result = restaurantRepository.findById(updatedRestaurant.id)
        assertEquals("Updated Restaurant", result.get().name)
    }

    @Test
    fun `should delete an existing restaurant`() {
        restaurantRepository.delete(savedRestaurant)
        val result = restaurantRepository.findById(savedRestaurant.id)
        assertEquals(result.isEmpty, true)
    }
}
