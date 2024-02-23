package br.com.blifood.domain.service

import br.com.blifood.domain.createState
import br.com.blifood.domain.exception.StateAlreadyExistsException
import br.com.blifood.domain.exception.StateInUseException
import br.com.blifood.domain.exception.StateNotFoundException
import br.com.blifood.domain.repository.StateRepository
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

class StateServiceTest : DescribeSpec({

    val stateRepository = mockk<StateRepository>()
    val stateService = StateService(stateRepository)

    val state = createState()
    val stateId = state.id

    describe("findAll") {
        it("should return page of states") {
            val pageable = mockk<Pageable>()
            val states = listOf(state)
            val page = PageImpl(states)
            every { stateRepository.findAll(pageable) } returns page

            val result = stateService.findAll(pageable)

            result shouldBe page
        }
    }

    describe("findOrThrow") {
        it("should return state if found") {
            every { stateRepository.findById(any()) } returns Optional.of(state)

            val result = stateService.findOrThrow(stateId)

            result shouldBe state
        }

        it("should throw exception if state not found") {
            every { stateRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<StateNotFoundException> { stateService.findOrThrow(stateId) }

            exception shouldBe StateNotFoundException()
        }
    }

    describe("save") {
        it("should save state if it does not already exist") {
            every { stateRepository.findByName(any()) } returns null
            every { stateRepository.save(any()) } returns state

            val result = stateService.save(state)

            result shouldBe state
            verify(exactly = 1) { stateRepository.save(state) }
        }

        it("should throw exception if state already exists") {
            val newState = createState(id = 0)
            every { stateRepository.findByName(any()) } returns state

            val exception = shouldThrow<StateAlreadyExistsException> { stateService.save(newState) }

            exception shouldBe StateAlreadyExistsException()
        }
    }

    describe("delete") {
        it("should delete the state if found") {
            every { stateRepository.findById(any()) } returns Optional.of(state)
            every { stateRepository.delete(any()) } just runs
            every { stateRepository.flush() } just runs

            stateService.delete(stateId)

            verify(exactly = 1) { stateRepository.delete(state) }
            verify(exactly = 1) { stateRepository.flush() }
        }

        it("should throw an exception if the state is not found") {
            every { stateRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<StateNotFoundException> { stateService.delete(stateId) }

            exception shouldBe StateNotFoundException()
        }

        it("should throw an exception if the state is in use") {
            every { stateRepository.findById(any()) } returns Optional.of(state)
            every { stateRepository.delete(any()) } throws DataIntegrityViolationException("")

            val exception = shouldThrow<StateInUseException> { stateService.delete(stateId) }

            exception shouldBe StateInUseException()
        }
    }
})
