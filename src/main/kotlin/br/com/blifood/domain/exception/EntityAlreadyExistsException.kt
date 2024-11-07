package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class EntityAlreadyExistsException(message: String) : BusinessException(message)

class StateAlreadyExistsException(
    message: String = Messages.get("state.already-exists")
) : EntityAlreadyExistsException(message)

class CityAlreadyExistsException(
    message: String = Messages.get("city.already-exists")
) : EntityAlreadyExistsException(message)

class CulinaryAlreadyExistsException(
    message: String = Messages.get("culinary.already-exists")
) : EntityAlreadyExistsException(message)

class PaymentMethodAlreadyExistsException(
    message: String = Messages.get("payment-method.already-exists")
) : EntityAlreadyExistsException(message)

class RestaurantAlreadyExistsException(
    message: String = Messages.get("restaurant.already-exists")
) : EntityAlreadyExistsException(message)

class ProductAlreadyExistsException(
    message: String = Messages.get("product.already-exists")
) : EntityAlreadyExistsException(message)

class UserAlreadyExistsException(
    message: String = Messages.get("user.already-exists")
) : EntityAlreadyExistsException(message)
