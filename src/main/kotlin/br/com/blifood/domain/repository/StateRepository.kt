package br.com.blifood.domain.repository

import br.com.blifood.domain.entity.State
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StateRepository : JpaRepository<State, Long> {
    fun findByName(name: String): State?
}
