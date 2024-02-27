package br.com.blifood.api.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.controller.PaymentMethodController
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.input.PaymentMethodInputModel
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.domain.createPaymentMethod
import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.service.PaymentMethodService
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

class PaymentMethodControllerTest : StringSpec({
    val paymentMethodService = mockk<PaymentMethodService>()
    val paymentMethodController = PaymentMethodController(paymentMethodService)
    val paymentMethod: PaymentMethod = createPaymentMethod()
    val paymentMethodId = paymentMethod.id

    fun createInputModel(paymentMethod: PaymentMethod) = PaymentMethodInputModel(description = paymentMethod.description)

    mockkStatic("br.com.blifood.api.v1.ResourceUtilsKt")
    every { addUriInResponseHeader(any()) } just runs
    every { getSecurityContextHolderUserId() } returns 1L

    "findAll should return PagedModel of PaymentMethodModel" {
        val pageable: Pageable = mockk()
        every { paymentMethodService.findAll(any()) } returns PageImpl(listOf(paymentMethod))

        val result: PagedModel<PaymentMethodModel> = paymentMethodController.findAll(pageable)
        result.content.size shouldBe 1
    }

    "findById should return PaymentMethodModel" {
        every { paymentMethodService.findOrThrow(any()) } returns paymentMethod

        val result: PaymentMethodModel = paymentMethodController.findById(paymentMethodId)
        result shouldBe paymentMethod.toModel()
    }

    "create should return created PaymentMethodModel" {
        every { paymentMethodService.save(any()) } returns paymentMethod

        val result: PaymentMethodModel = paymentMethodController.create(createInputModel(paymentMethod))
        result shouldBe paymentMethod.toModel()
    }

    "alter should return altered PaymentMethodModel" {
        every { paymentMethodService.findOrThrow(any()) } returns paymentMethod
        every { paymentMethodService.save(any()) } returns paymentMethod

        val result: PaymentMethodModel = paymentMethodController.alter(paymentMethodId, createInputModel(paymentMethod))
        result shouldBe paymentMethod.toModel()
    }

    "delete should return NO_CONTENT" {
        every { paymentMethodService.delete(any()) } just runs

        paymentMethodController.delete(paymentMethodId)
        verify { paymentMethodService.delete(paymentMethodId) }
    }
})
