package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findByNameAndAddressCityId(name: String, cityId: Long): Restaurant?
}
