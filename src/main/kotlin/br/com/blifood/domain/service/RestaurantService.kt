package br.com.blifood.domain.service

import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.exception.RestaurantAlreadyExistsException
import br.com.blifood.domain.exception.RestaurantInUseException
import br.com.blifood.domain.exception.RestaurantNotFoundException
import br.com.blifood.domain.exception.UserNotAuthorizedException
import br.com.blifood.domain.repository.RestaurantRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val cityService: CityService,
    private val culinaryService: CulinaryService,
    private val paymentMethodService: PaymentMethodService,
    private val userService: UserService
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Restaurant> {
        return restaurantRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findOrThrow(restaurantId: Long): Restaurant {
        return restaurantRepository.findById(restaurantId).orElseThrow { RestaurantNotFoundException() }
    }

    @Transactional
    fun save(restaurant: Restaurant, userId: Long): Restaurant {
        validateManager(restaurant, userId)
        restaurant.culinary = culinaryService.findOrThrow(restaurant.culinary.id)
        restaurant.address.city = cityService.findOrThrow(restaurant.address.city.id)
        restaurantRepository.findByNameAndAddressCityId(restaurant.name, restaurant.address.city.id)?.let {
            if (it.id != restaurant.id) throw RestaurantAlreadyExistsException()
        }
        return restaurantRepository.save(restaurant)
    }

    @Transactional(rollbackFor = [RestaurantInUseException::class])
    fun delete(restaurantId: Long, userId: Long) {
        validateManager(restaurantId, userId)
        runCatching {
            restaurantRepository.delete(findOrThrow(restaurantId))
            restaurantRepository.flush()
        }.onFailure { // it: Throwable ->
            when (it) {
                is DataIntegrityViolationException -> throw RestaurantInUseException()
                else -> throw it
            }
        }
    }

    @Transactional
    fun activate(restaurantId: Long, userId: Long) {
        val restaurant = findOrThrow(restaurantId)
        validateManager(restaurant, userId)
        restaurantRepository.save(restaurant.activate())
    }

    @Transactional
    fun inactivate(restaurantId: Long, userId: Long) {
        val restaurant = findOrThrow(restaurantId)
        validateManager(restaurant, userId)
        restaurantRepository.save(restaurant.inactivate())
    }

    @Transactional
    fun open(restaurantId: Long, userId: Long) {
        val restaurant = findOrThrow(restaurantId)
        validateManager(restaurant, userId)
        restaurantRepository.save(restaurant.open())
    }

    @Transactional
    fun close(restaurantId: Long) {
        val restaurant = findOrThrow(restaurantId)
        restaurantRepository.save(restaurant.close())
    }

    @Transactional
    fun addPaymentMethod(restaurantId: Long, userId: Long, paymentMethodId: Long) {
        val restaurant = findOrThrow(restaurantId)
        validateManager(restaurant, userId)
        val paymentMethod = paymentMethodService.findOrThrow(paymentMethodId)
        restaurantRepository.save(restaurant.addPaymentMethod(paymentMethod))
    }

    @Transactional
    fun removePaymentMethod(restaurantId: Long, userId: Long, paymentMethodId: Long) {
        val restaurant = findOrThrow(restaurantId)
        validateManager(restaurant, userId)
        val paymentMethod = paymentMethodService.findOrThrow(paymentMethodId)
        restaurantRepository.save(restaurant.removePaymentMethod(paymentMethod))
    }

    @Transactional
    fun addManager(restaurantId: Long, userId: Long) {
        val restaurant = findOrThrow(restaurantId)
        val user = userService.findOrThrow(userId)
        validateManager(restaurant, user)
        restaurantRepository.save(restaurant.addManager(user))
    }

    @Transactional
    fun removeManager(restaurantId: Long, userId: Long) {
        val restaurant = findOrThrow(restaurantId)
        val user = userService.findOrThrow(userId)
        validateManager(restaurant, user)
        restaurantRepository.save(restaurant.removeManager(user))
    }

    @Transactional(readOnly = true)
    fun findAllPaymentMethods(restaurantId: Long): Set<PaymentMethod> {
        return findOrThrow(restaurantId).paymentsMethods
    }

    @Transactional(readOnly = true)
    fun findAllManagers(restaurantId: Long): Set<User> {
        return findOrThrow(restaurantId).managers
    }

    private fun validateManager(restaurantId: Long, userId: Long) {
        val restaurant = findOrThrow(restaurantId)
        val user = userService.findOrThrow(userId)
        return validateManager(restaurant, user)
    }

    private fun validateManager(restaurant: Restaurant, userId: Long) {
        val user = userService.findOrThrow(userId)
        return validateManager(restaurant, user)
    }

    private fun validateManager(restaurant: Restaurant, user: User) {
        val isManager = user.profile == UserProfile.ADMIN || (user.profile == UserProfile.MANAGER && restaurant.managers.contains(user))
        if (!isManager) throw UserNotAuthorizedException()
    }
}
