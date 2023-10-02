package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.Culinary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CulinaryRepository : JpaRepository<Culinary, Long> {
    fun findByName(name: String): Culinary?
}
