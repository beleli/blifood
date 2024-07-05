package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class EntityInUseException(message: String?) : BusinessException(message)

class StateInUseException(
    message: String = Messages.get("state.inUse")
) : EntityInUseException(message)

class CityInUseException(
    message: String = Messages.get("city.inUse")
) : EntityInUseException(message)

class CulinaryInUseException(
    message: String = Messages.get("culinary.inUse")
) : EntityInUseException(message)

class PaymentMethodInUseException(
    message: String = Messages.get("paymentMethod.inUse")
) : EntityInUseException(message)

class RestaurantInUseException(
    message: String = Messages.get("restaurant.inUse")
) : EntityInUseException(message)

class ProductInUseException(
    message: String = Messages.get("product.inUse")
) : EntityInUseException(message)
