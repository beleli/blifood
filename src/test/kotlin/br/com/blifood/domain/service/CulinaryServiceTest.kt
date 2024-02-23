package br.com.blifood.domain.service

import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.exception.CulinaryAlreadyExistsException
import br.com.blifood.domain.exception.CulinaryInUseException
import br.com.blifood.domain.exception.CulinaryNotFoundException
import br.com.blifood.domain.repository.CulinaryRepository
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

class CulinaryServiceTest : DescribeSpec({

    val culinaryRepository = mockk<CulinaryRepository>()
    val culinaryService = CulinaryService(culinaryRepository)

    val culinary = createCulinary()
    val culinaryId = culinary.id

    describe("findAll") {
        it("should return page of culinary") {
            val pageable = mockk<Pageable>()
            val culinaryList = listOf(culinary)
            val page = PageImpl(culinaryList)
            every { culinaryRepository.findAll(pageable) } returns page

            val result = culinaryService.findAll(pageable)

            result shouldBe page
        }
    }

    describe("findOrThrow") {
        it("should return culinary if found") {
            every { culinaryRepository.findById(any()) } returns Optional.of(culinary)

            val result = culinaryService.findOrThrow(culinaryId)

            result shouldBe culinary
        }

        it("should throw exception if culinary not found") {
            every { culinaryRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<CulinaryNotFoundException> { culinaryService.findOrThrow(culinaryId) }

            exception shouldBe CulinaryNotFoundException()
        }
    }

    describe("save") {
        it("should save culinary if it does not already exist") {
            every { culinaryRepository.findByName(any()) } returns null
            every { culinaryRepository.save(any()) } returns culinary

            val result = culinaryService.save(culinary)

            result shouldBe culinary
            verify(exactly = 1) { culinaryRepository.save(culinary) }
        }

        it("should throw exception if culinary already exists") {
            val newCulinary = createCulinary(id = 0)
            every { culinaryRepository.findByName(any()) } returns culinary

            val exception = shouldThrow<CulinaryAlreadyExistsException> { culinaryService.save(newCulinary) }

            exception shouldBe CulinaryAlreadyExistsException()
        }
    }

    describe("delete") {
        it("should delete culinary if found") {
            every { culinaryRepository.findById(any()) } returns Optional.of(culinary)
            every { culinaryRepository.delete(any()) } just runs
            every { culinaryRepository.flush() } just runs

            culinaryService.delete(culinaryId)

            verify(exactly = 1) { culinaryRepository.delete(culinary) }
            verify(exactly = 1) { culinaryRepository.flush() }
        }

        it("should throw exception if culinary not found") {
            every { culinaryRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<CulinaryNotFoundException> { culinaryService.delete(culinaryId) }

            exception shouldBe CulinaryNotFoundException()
        }

        it("should throw exception if culinary is in use") {
            every { culinaryRepository.findById(any()) } returns Optional.of(culinary)
            every { culinaryRepository.delete(any()) } throws DataIntegrityViolationException("")

            val exception = shouldThrow<CulinaryInUseException> { culinaryService.delete(culinaryId) }

            exception shouldBe CulinaryInUseException()
        }
    }
})
