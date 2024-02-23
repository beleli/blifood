package br.com.blifood.domain.service

import br.com.blifood.domain.createCity
import br.com.blifood.domain.exception.CityAlreadyExistsException
import br.com.blifood.domain.exception.CityInUseException
import br.com.blifood.domain.exception.CityNotFoundException
import br.com.blifood.domain.repository.CityRepository
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

class CityServiceTest : DescribeSpec({

    val cityRepository = mockk<CityRepository>()
    val stateService = mockk<StateService>()
    val cityService = CityService(cityRepository, stateService)

    val city = createCity()
    val cityId = city.id

    describe("findAll") {
        it("should return page of cities") {
            val pageable = mockk<Pageable>()
            val cities = listOf(city)
            val page = PageImpl(cities)
            every { cityRepository.findAll(pageable) } returns page

            val result = cityService.findAll(pageable)

            result shouldBe page
        }
    }

    describe("findOrThrow") {
        it("should return city if found") {
            every { cityRepository.findById(any()) } returns Optional.of(city)

            val result = cityService.findOrThrow(cityId)

            result shouldBe city
        }

        it("should throw exception if city not found") {
            every { cityRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<CityNotFoundException> { cityService.findOrThrow(cityId) }

            exception shouldBe CityNotFoundException()
        }
    }

    describe("save") {
        it("should save city if it does not already exist") {
            every { stateService.findOrThrow(any()) } returns city.state
            every { cityRepository.findByNameAndStateId(any(), any()) } returns null
            every { cityRepository.save(any()) } returns city

            val result = cityService.save(city)

            result shouldBe city
            verify(exactly = 1) { cityRepository.save(city) }
        }

        it("should throw exception if city already exists") {
            val newCity = createCity(id = 0)
            every { stateService.findOrThrow(any()) } returns city.state
            every { cityRepository.findByNameAndStateId(any(), any()) } returns city

            val exception = shouldThrow<CityAlreadyExistsException> { cityService.save(newCity) }

            exception shouldBe CityAlreadyExistsException()
        }
    }

    describe("delete") {
        it("should delete city if found") {
            every { cityRepository.findById(any()) } returns Optional.of(city)
            every { cityRepository.delete(any()) } just runs
            every { cityRepository.flush() } just runs

            cityService.delete(cityId)

            verify(exactly = 1) { cityRepository.delete(city) }
            verify(exactly = 1) { cityRepository.flush() }
        }

        it("should throw exception if city not found") {
            every { cityRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<CityNotFoundException> { cityService.delete(cityId) }

            exception shouldBe CityNotFoundException()
        }

        it("should throw exception if city is in use") {
            every { cityRepository.findById(any()) } returns Optional.of(city)
            every { cityRepository.delete(any()) } throws DataIntegrityViolationException("")

            val exception = shouldThrow<CityInUseException> { cityService.delete(cityId) }

            exception shouldBe CityInUseException()
        }
    }
})
