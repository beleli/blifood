package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class EntityAlreadyExistsException(
    message: String = Messages.get("entity.alreadyExists")
) : BusinessException(message)

class StateAlreadyExistsException(
    message: String = Messages.get("state.alreadyExists")
) : EntityAlreadyExistsException(message)

class CityAlreadyExistsException(
    message: String = Messages.get("city.alreadyExists")
) : EntityAlreadyExistsException(message)

class CulinaryAlreadyExistsException(
    message: String = Messages.get("culinary.alreadyExists")
) : EntityAlreadyExistsException(message)

class PaymentMethodAlreadyExistsException(
    message: String = Messages.get("paymentMethod.alreadyExists")
) : EntityAlreadyExistsException(message)

class RestaurantAlreadyExistsException(
    message: String = Messages.get("restaurant.alreadyExists")
) : EntityAlreadyExistsException(message)

class ProductAlreadyExistsException(
    message: String = Messages.get("product.alreadyExists")
) : EntityAlreadyExistsException(message)

class UserAlreadyExistsException(
    message: String = Messages.get("user.alreadyExists")
) : EntityAlreadyExistsException(message)
