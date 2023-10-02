package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class BusinessException(message: String?) : Exception(message)

class UserInvalidPasswordException(
    message: String = Messages.get("user.invalidPassword")
) : BusinessException(message)

class OrderInvalidPaymentMethodException(
    message: String = Messages.get("order.invalidPaymentMethod")
) : BusinessException(message)

class OrderNotConfirmedException(
    message: String = Messages.get("order.notConfirmed")
) : BusinessException(message)

class OrderNotCanceledException(
    message: String = Messages.get("order.notCanceled")
) : BusinessException(message)

class OrderNotDeliveredException(
    message: String = Messages.get("order.notDelivered")
) : BusinessException(message)
