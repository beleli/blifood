package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class EntityInUseException(message: String) : BusinessException(message)

class StateInUseException(
    message: String = Messages.get("state.in-use")
) : EntityInUseException(message)

class CityInUseException(
    message: String = Messages.get("city.in-use")
) : EntityInUseException(message)

class CulinaryInUseException(
    message: String = Messages.get("culinary.in-use")
) : EntityInUseException(message)

class PaymentMethodInUseException(
    message: String = Messages.get("payment-method.in-use")
) : EntityInUseException(message)

class RestaurantInUseException(
    message: String = Messages.get("restaurant.in-use")
) : EntityInUseException(message)

class ProductInUseException(
    message: String = Messages.get("product.in-use")
) : EntityInUseException(message)
