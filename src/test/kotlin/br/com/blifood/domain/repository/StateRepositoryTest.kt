package br.com.blifood.domain.repository

import br.com.blifood.domain.createState
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
class StateRepositoryTest {

    @Autowired
    private lateinit var stateRepository: StateRepository

    private var savedState: State = createState()

    @BeforeEach
    fun setUp() {
        savedState = stateRepository.save(createState())
    }

    @Test
    fun `should find state by name`() {
        val result = stateRepository.findByName(savedState.name)
        assertEquals(savedState, result)
    }

    @Test
    fun `should return null if state not found by name`() {
        val result = stateRepository.findByName("UK")
        assertNull(result)
    }

    @Test
    fun `should insert a new state`() {
        val result = stateRepository.findById(savedState.id)
        assertEquals(savedState, result.get())
    }

    @Test
    fun `should update an existing state`() {
        savedState.name = "Updated ST"

        val updatedState = stateRepository.save(savedState)

        val result = stateRepository.findById(updatedState.id)
        assertEquals("Updated ST", result.get().name)
    }

    @Test
    fun `should delete an existing state`() {
        stateRepository.delete(savedState)
        val result = stateRepository.findById(savedState.id)
        assertEquals(result.isEmpty, true)
    }
}
