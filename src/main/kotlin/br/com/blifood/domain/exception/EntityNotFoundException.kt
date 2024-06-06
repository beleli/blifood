package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class EntityNotFoundException(message: String?) : BusinessException(message)

class StateNotFoundException(
    message: String = Messages.get("state.notFound")
) : EntityNotFoundException(message)

class CityNotFoundException(
    message: String = Messages.get("city.notFound")
) : EntityNotFoundException(message)

class CulinaryNotFoundException(
    message: String = Messages.get("culinary.notFound")
) : EntityNotFoundException(message)

class PaymentTypeNotFoundException(
    message: String = Messages.get("paymentMethod.notFound")
) : EntityNotFoundException(message)

class RestaurantNotFoundException(
    message: String = Messages.get("restaurant.notFound")
) : EntityNotFoundException(message)

class ProductNotFoundException(
    message: String = Messages.get("product.notFound")
) : EntityNotFoundException(message)

class ProductImageNotFoundException(
    message: String = Messages.get("productImage.notFound")
) : EntityNotFoundException(message)

class UserNotFoundException(
    message: String = Messages.get("user.notFound")
) : EntityNotFoundException(message)

class OrderNotFoundException(
    message: String = Messages.get("order.notFound")
) : EntityNotFoundException(message)
