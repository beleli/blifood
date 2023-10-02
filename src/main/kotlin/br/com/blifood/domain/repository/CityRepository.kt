package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.City
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CityRepository : JpaRepository<City, Long> {
    fun findByNameAndStateId(name: String, stateId: Long): City?
}
