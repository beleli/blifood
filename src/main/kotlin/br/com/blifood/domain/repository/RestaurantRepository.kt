package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.Restaurant
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findByNameAndAddressCityId(name: String, cityId: Long): Restaurant?
}
