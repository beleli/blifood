package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.CityController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.input.CityInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createCity
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.StateNotFoundException
import br.com.blifood.domain.service.CityService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel

class CityControllerTest : StringSpec({
    val cityService = mockk<CityService>()
    val cityController = CityController(cityService)
    val city: City = createCity()
    val cityId = city.id

    fun createInputModel(city: City) = CityInputModel(name = city.name, stateId = city.state.id)

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return PagedModel of CityModel" {
        val pageable: Pageable = mockk()
        every { cityService.findAll(any()) } returns PageImpl(listOf(city))

        val result: PagedModel<CityModel> = cityController.findAll(pageable)
        result.content.size shouldBe 1
    }

    "findById should return CityModel" {
        every { cityService.findOrThrow(any()) } returns city

        val result: CityModel = cityController.findById(cityId)
        result shouldBe city.toModel()
    }

    "create should return created CityModel" {
        every { cityService.save(any()) } returns city

        val result: CityModel = cityController.create(createInputModel(city))
        result shouldBe city.toModel()
    }

    "create should throw BusinessException" {
        every { cityService.save(any()) } throws StateNotFoundException()

        val exception = shouldThrow<BusinessException> { cityController.create(createInputModel(city)) }
        exception shouldBe BusinessException(StateNotFoundException().message)
    }

    "create should throw Exception" {
        every { cityService.save(any()) } throws Throwable()

        val exception = shouldThrow<Throwable> { cityController.create(createInputModel(city)) }
        exception shouldBe Throwable()
    }

    "alter should return altered CityModel" {
        every { cityService.findOrThrow(any()) } returns city
        every { cityService.save(any()) } returns city

        val result: CityModel = cityController.alter(cityId, createInputModel(city))
        result shouldBe city.toModel()
    }

    "delete should return NO_CONTENT" {
        every { cityService.delete(any()) } just runs

        cityController.delete(cityId)
        verify { cityService.delete(cityId) }
    }
})
