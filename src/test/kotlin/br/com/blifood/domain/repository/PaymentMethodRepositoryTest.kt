package br.com.blifood.domain.repository

import br.com.blifood.domain.createPaymentMethod
import br.com.blifood.domain.entity.PaymentMethod
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
class PaymentMethodRepositoryTest {

    @Autowired
    private lateinit var paymentMethodRepository: PaymentMethodRepository

    private var savedPaymentMethod: PaymentMethod = createPaymentMethod()

    @BeforeEach
    fun setUp() {
        savedPaymentMethod = paymentMethodRepository.save(createPaymentMethod())
    }

    @Test
    fun `should find payment method by description`() {
        val result = paymentMethodRepository.findByDescription(savedPaymentMethod.description)
        assertEquals(savedPaymentMethod, result)
    }

    @Test
    fun `should return null if payment method not found by description`() {
        val result = paymentMethodRepository.findByDescription("Unknown Description")
        assertNull(result)
    }

    @Test
    fun `should insert a new payment method`() {
        val result = paymentMethodRepository.findById(savedPaymentMethod.id)
        assertEquals(savedPaymentMethod, result.get())
    }

    @Test
    fun `should update an existing payment method`() {
        savedPaymentMethod.description = "Updated Description"

        val updatedPaymentMethod = paymentMethodRepository.save(savedPaymentMethod)

        val result = paymentMethodRepository.findById(updatedPaymentMethod.id)
        assertEquals("Updated Description", result.get().description)
    }

    @Test
    fun `should delete an existing payment method`() {
        paymentMethodRepository.delete(savedPaymentMethod)
        val result = paymentMethodRepository.findById(savedPaymentMethod.id)
        assertEquals(result.isEmpty, true)
    }
}
