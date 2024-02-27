package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.StateController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.StateModel
import br.com.blifood.api.v1.model.input.StateInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createState
import br.com.blifood.domain.entity.State
import br.com.blifood.domain.service.StateService
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

class StateControllerTest : StringSpec({
    val stateService = mockk<StateService>()
    val stateController = StateController(stateService)
    val state: State = createState()
    val stateId = state.id

    fun createInputModel(state: State) = StateInputModel(name = state.name)

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return PagedModel of StateModel" {
        val pageable: Pageable = mockk()
        every { stateService.findAll(any()) } returns PageImpl(listOf(state))

        val result: PagedModel<StateModel> = stateController.findAll(pageable)
        result.content.size shouldBe 1
    }

    "findById should return StateModel" {
        every { stateService.findOrThrow(any()) } returns state

        val result: StateModel = stateController.findById(stateId)
        result shouldBe state.toModel()
    }

    "create should return created StateModel" {
        every { stateService.save(any()) } returns state

        val result: StateModel = stateController.create(createInputModel(state))
        result shouldBe state.toModel()
    }

    "alter should return altered StateModel" {
        every { stateService.findOrThrow(any()) } returns state
        every { stateService.save(any()) } returns state

        val result: StateModel = stateController.alter(stateId, createInputModel(state))
        result shouldBe state.toModel()
    }

    "delete should return NO_CONTENT" {
        every { stateService.delete(any()) } just runs

        stateController.delete(stateId)
        verify { stateService.delete(stateId) }
    }
})
