package br.com.blifood.api.v1.model.input

import br.com.blifood.core.log.Loggable
import br.com.blifood.domain.entity.PaymentMethod
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PaymentMethodInputModel(

    @Schema(example = "Cartão de Credito")
    @field:NotBlank(message = "constraints.not-blank")
    @field:Size(max = 60, message = "constraints.size")
    val description: String?
) : Loggable

fun PaymentMethodInputModel.toEntity() = PaymentMethod(description = description!!)

fun PaymentMethod.applyModel(paymentMethodInputModel: PaymentMethodInputModel) = this.apply {
    description = paymentMethodInputModel.description!!
}
