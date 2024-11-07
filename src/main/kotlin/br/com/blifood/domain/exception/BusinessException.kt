package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class BusinessException(message: String?) : Exception(message)

class UserInvalidPasswordException(
    message: String = Messages.get("user.invalid-password")
) : BusinessException(message)

class UserNotAuthorizedException(
    message: String = Messages.get("user.not-authorized")
) : BusinessException(message)

class OrderInvalidPaymentMethodException(
    message: String = Messages.get("order.invalid-payment-method")
) : BusinessException(message)

class OrderNotConfirmedException(
    message: String = Messages.get("order.not-confirmed")
) : BusinessException(message)

class OrderNotCanceledException(
    message: String = Messages.get("order.not-canceled")
) : BusinessException(message)

class OrderNotDeliveredException(
    message: String = Messages.get("order.not-delivered")
) : BusinessException(message)
