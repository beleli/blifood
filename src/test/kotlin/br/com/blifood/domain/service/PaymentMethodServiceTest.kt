package br.com.blifood.domain.service

import br.com.blifood.domain.createPaymentMethod
import br.com.blifood.domain.exception.PaymentMethodAlreadyExistsException
import br.com.blifood.domain.exception.PaymentMethodInUseException
import br.com.blifood.domain.exception.PaymentTypeNotFoundException
import br.com.blifood.domain.repository.PaymentMethodRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional

class PaymentMethodServiceTest : DescribeSpec({

    val paymentMethodRepository = mockk<PaymentMethodRepository>()
    val paymentMethodService = PaymentMethodService(paymentMethodRepository)

    val paymentMethod = createPaymentMethod()
    val paymentMethodId = paymentMethod.id

    describe("findAll") {
        it("should return page of payment methods") {
            val pageable = mockk<Pageable>()
            val paymentMethods = listOf(paymentMethod)
            val page = PageImpl(paymentMethods)
            every { paymentMethodRepository.findAll(pageable) } returns page

            val result = paymentMethodService.findAll(pageable)

            result shouldBe page
        }
    }

    describe("findOrThrow") {
        it("should return payment method if found") {
            every { paymentMethodRepository.findById(any()) } returns Optional.of(paymentMethod)

            val result = paymentMethodService.findOrThrow(paymentMethodId)

            result shouldBe paymentMethod
        }

        it("should throw exception if payment method not found") {
            every { paymentMethodRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<PaymentTypeNotFoundException> { paymentMethodService.findOrThrow(paymentMethodId) }

            exception shouldBe PaymentTypeNotFoundException()
        }
    }

    describe("save") {
        it("should save payment method if it does not already exist") {
            every { paymentMethodRepository.findByDescription(any()) } returns null
            every { paymentMethodRepository.save(any()) } returns paymentMethod

            val result = paymentMethodService.save(paymentMethod)

            result shouldBe paymentMethod
            verify(exactly = 1) { paymentMethodRepository.save(paymentMethod) }
        }

        it("should throw exception if payment method already exists") {
            val newPaymentMethod = createPaymentMethod(id = 0)
            every { paymentMethodRepository.findByDescription(any()) } returns paymentMethod

            val exception = shouldThrow<PaymentMethodAlreadyExistsException> { paymentMethodService.save(newPaymentMethod) }

            exception shouldBe PaymentMethodAlreadyExistsException()
        }
    }

    describe("delete") {
        it("should delete payment method if found") {
            every { paymentMethodRepository.findById(any()) } returns Optional.of(paymentMethod)
            every { paymentMethodRepository.delete(any()) } just runs
            every { paymentMethodRepository.flush() } just runs

            paymentMethodService.delete(paymentMethodId)

            verify(exactly = 1) { paymentMethodRepository.delete(paymentMethod) }
            verify(exactly = 1) { paymentMethodRepository.flush() }
        }

        it("should throw exception if payment method not found") {
            every { paymentMethodRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<PaymentTypeNotFoundException> { paymentMethodService.delete(paymentMethodId) }

            exception shouldBe PaymentTypeNotFoundException()
        }

        it("should throw exception if payment method is in use") {
            every { paymentMethodRepository.findById(any()) } returns Optional.of(paymentMethod)
            every { paymentMethodRepository.delete(any()) } throws DataIntegrityViolationException("")

            val exception = shouldThrow<PaymentMethodInUseException> { paymentMethodService.delete(paymentMethodId) }

            exception shouldBe PaymentMethodInUseException()
        }
    }
})
