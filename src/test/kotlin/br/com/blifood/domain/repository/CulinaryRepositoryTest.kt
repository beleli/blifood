package br.com.blifood.domain.repository

import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.entity.Culinary
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
class CulinaryRepositoryTest {

    @Autowired
    private lateinit var culinaryRepository: CulinaryRepository

    private var savedCulinary: Culinary = createCulinary()

    @BeforeEach
    fun setUp() {
        savedCulinary = culinaryRepository.save(createCulinary())
    }

    @Test
    fun `should find culinary by name`() {
        val result = culinaryRepository.findByName(savedCulinary.name)
        assertEquals(savedCulinary, result)
    }

    @Test
    fun `should return null if culinary not found by name`() {
        val result = culinaryRepository.findByName("UK")
        assertNull(result)
    }

    @Test
    fun `should insert a new culinary`() {
        val result = culinaryRepository.findById(savedCulinary.id)
        assertEquals(savedCulinary, result.get())
    }

    @Test
    fun `should update an existing culinary`() {
        savedCulinary.name = "Updated ST"

        val updatedCulinary = culinaryRepository.save(savedCulinary)

        val result = culinaryRepository.findById(updatedCulinary.id)
        assertEquals("Updated ST", result.get().name)
    }

    @Test
    fun `should delete an existing culinary`() {
        culinaryRepository.delete(savedCulinary)
        val result = culinaryRepository.findById(savedCulinary.id)
        assertEquals(result.isEmpty, true)
    }
}
