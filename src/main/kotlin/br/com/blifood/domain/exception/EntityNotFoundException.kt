package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class EntityNotFoundException(message: String) : BusinessException(message)

class StateNotFoundException(
    message: String = Messages.get("state.not-found")
) : EntityNotFoundException(message)

class CityNotFoundException(
    message: String = Messages.get("city.not-found")
) : EntityNotFoundException(message)

class CulinaryNotFoundException(
    message: String = Messages.get("culinary.not-found")
) : EntityNotFoundException(message)

class PaymentTypeNotFoundException(
    message: String = Messages.get("payment-method.not-found")
) : EntityNotFoundException(message)

class RestaurantNotFoundException(
    message: String = Messages.get("restaurant.not-found")
) : EntityNotFoundException(message)

class ProductNotFoundException(
    message: String = Messages.get("product.not-found")
) : EntityNotFoundException(message)

class ProductImageNotFoundException(
    message: String = Messages.get("product-image.not-found")
) : EntityNotFoundException(message)

class UserNotFoundException(
    message: String = Messages.get("user.not-found")
) : EntityNotFoundException(message)

class OrderNotFoundException(
    message: String = Messages.get("order.not-found")
) : EntityNotFoundException(message)
