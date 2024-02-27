package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.CulinaryController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.input.CulinaryInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createCulinary
import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.service.CulinaryService
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

class CulinaryControllerTest : StringSpec({
    val culinaryService = mockk<CulinaryService>()
    val culinaryController = CulinaryController(culinaryService)
    val culinary: Culinary = createCulinary()
    val culinaryId = culinary.id

    fun createInputModel(culinary: Culinary) = CulinaryInputModel(name = culinary.name)

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return PagedModel of CulinaryModel" {
        val pageable: Pageable = mockk()
        every { culinaryService.findAll(any()) } returns PageImpl(listOf(culinary))

        val result: PagedModel<CulinaryModel> = culinaryController.findAll(pageable)
        result.content.size shouldBe 1
    }

    "findById should return CulinaryModel" {
        every { culinaryService.findOrThrow(any()) } returns culinary

        val result: CulinaryModel = culinaryController.findById(culinaryId)
        result shouldBe culinary.toModel()
    }

    "create should return created CulinaryModel" {
        every { culinaryService.save(any()) } returns culinary

        val result: CulinaryModel = culinaryController.create(createInputModel(culinary))
        result shouldBe culinary.toModel()
    }

    "alter should return altered CulinaryModel" {
        every { culinaryService.findOrThrow(any()) } returns culinary
        every { culinaryService.save(any()) } returns culinary

        val result: CulinaryModel = culinaryController.alter(culinaryId, createInputModel(culinary))
        result shouldBe culinary.toModel()
    }

    "delete should return NO_CONTENT" {
        every { culinaryService.delete(any()) } just runs

        culinaryController.delete(culinaryId)
        verify { culinaryService.delete(culinaryId) }
    }
})
