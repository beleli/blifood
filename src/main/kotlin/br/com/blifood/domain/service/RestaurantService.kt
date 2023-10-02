package br.com.blifood.domain.service

import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.exception.RestaurantAlreadyExistsException
import br.com.blifood.domain.exception.RestaurantInUseException
import br.com.blifood.domain.exception.RestaurantNotFoundException
import br.com.blifood.domain.repository.RestaurantRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val cityService: CityService,
    private val culinaryService: CulinaryService,
    private val paymentMethodService: PaymentMethodService
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Restaurant> {
        return restaurantRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findOrThrow(id: Long): Restaurant {
        return restaurantRepository.findById(id).orElseThrow { RestaurantNotFoundException() }
    }

    @Transactional
    fun save(restaurant: Restaurant): Restaurant {
        restaurant.culinary = culinaryService.findOrThrow(restaurant.culinary.id)
        restaurant.address.city = cityService.findOrThrow(restaurant.address.city.id)
        restaurantRepository.findByNameAndAddressCityId(restaurant.name, restaurant.address.city.id)?.let {
            if (it.id != restaurant.id) throw RestaurantAlreadyExistsException()
        }
        return restaurantRepository.save(restaurant)
    }

    @Transactional(rollbackFor = [RestaurantInUseException::class])
    fun delete(id: Long) {
        runCatching {
            restaurantRepository.delete(findOrThrow(id))
            restaurantRepository.flush()
        }.onFailure { // it: Throwable ->
            when (it) {
                is DataIntegrityViolationException -> throw RestaurantInUseException()
                else -> throw it
            }
        }
    }

    @Transactional()
    fun activate(id: Long) {
        val restaurant = findOrThrow(id)
        restaurantRepository.save(restaurant.activate())
    }

    @Transactional()
    fun inactivate(id: Long) {
        val restaurant = findOrThrow(id)
        restaurantRepository.save(restaurant.inactivate())
    }

    @Transactional()
    fun open(id: Long) {
        val restaurant = findOrThrow(id)
        restaurantRepository.save(restaurant.open())
    }

    @Transactional()
    fun close(id: Long) {
        val restaurant = findOrThrow(id)
        restaurantRepository.save(restaurant.close())
    }

    @Transactional
    fun addPaymentMethod(restaurantId: Long, paymentMethodId: Long) {
        val restaurant = findOrThrow(restaurantId)
        val paymentMethod = paymentMethodService.findOrThrow(paymentMethodId)
        restaurantRepository.save(restaurant.addPaymentMethod(paymentMethod))
    }

    @Transactional
    fun removePaymentMethod(restaurantId: Long, paymentMethodId: Long) {
        val restaurant = findOrThrow(restaurantId)
        val paymentMethod = paymentMethodService.findOrThrow(paymentMethodId)
        restaurantRepository.save(restaurant.removePaymentMethod(paymentMethod))
    }
}
