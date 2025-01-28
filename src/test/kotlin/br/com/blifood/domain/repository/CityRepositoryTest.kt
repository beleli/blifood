package br.com.blifood.domain.repository

import br.com.blifood.domain.createCity
import br.com.blifood.domain.createState
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.State
import org.junit.jupiter.api.Assertions.assertEquals
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
class CityRepositoryTest {

    @Autowired
    private lateinit var cityRepository: CityRepository

    @Autowired
    private lateinit var stateRepository: StateRepository

    private var savedState: State = createState()
    private var savedCity: City = createCity()

    @BeforeEach
    fun setUp() {
        savedState = stateRepository.save(createState())
        savedCity = cityRepository.save(createCity(state = savedState))
    }

    @Test
    fun `should find city by name`() {
        val result = cityRepository.findByNameAndStateId(savedCity.name, savedCity.state.id)
        assertEquals(savedCity, result)
    }

    @Test
    fun `should insert a new city`() {
        val result = cityRepository.findById(savedCity.id)
        assertEquals(savedCity, result.get())
    }

    @Test
    fun `should update an existing city`() {
        savedCity.name = "Updated City"

        val updatedCity = cityRepository.save(savedCity)

        val result = cityRepository.findById(updatedCity.id)
        assertEquals("Updated City", result.get().name)
    }

    @Test
    fun `should delete an existing city`() {
        cityRepository.delete(savedCity)
        val result = cityRepository.findById(savedCity.id)
        assertEquals(result.isEmpty, true)
    }
}
