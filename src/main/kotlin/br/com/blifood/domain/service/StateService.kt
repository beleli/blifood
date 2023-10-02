package br.com.blifood.domain.service

import br.com.blifood.domain.entity.State
import br.com.blifood.domain.exception.StateAlreadyExistsException
import br.com.blifood.domain.exception.StateInUseException
import br.com.blifood.domain.exception.StateNotFoundException
import br.com.blifood.domain.repository.StateRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StateService(
    private val stateRepository: StateRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<State> {
        return stateRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findOrThrow(id: Long): State {
        return stateRepository.findById(id).orElseThrow { StateNotFoundException() }
    }

    @Transactional
    fun save(state: State): State {
        stateRepository.findByName(state.name)?.let {
            if (it.id != state.id) throw StateAlreadyExistsException()
        }
        return stateRepository.save(state)
    }

    @Transactional(rollbackFor = [StateInUseException::class])
    fun delete(id: Long) {
        runCatching {
            stateRepository.delete(findOrThrow(id))
            stateRepository.flush()
        }.onFailure { // it: Throwable ->
            when (it) {
                is DataIntegrityViolationException -> throw StateInUseException()
                else -> throw it
            }
        }
    }
}
