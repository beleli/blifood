package br.com.blifood.domain.service

import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.exception.CulinaryAlreadyExistsException
import br.com.blifood.domain.exception.CulinaryInUseException
import br.com.blifood.domain.exception.CulinaryNotFoundException
import br.com.blifood.domain.repository.CulinaryRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CulinaryService(
    private val culinaryRepository: CulinaryRepository
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<Culinary> {
        return culinaryRepository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    fun findOrThrow(id: Long): Culinary {
        return culinaryRepository.findById(id).orElseThrow { CulinaryNotFoundException() }
    }

    @Transactional
    fun save(state: Culinary): Culinary {
        culinaryRepository.findByName(state.name)?.let {
            if (it.id != state.id) throw CulinaryAlreadyExistsException()
        }
        return culinaryRepository.save(state)
    }

    @Transactional(rollbackFor = [CulinaryInUseException::class])
    fun delete(id: Long) {
        runCatching {
            culinaryRepository.delete(findOrThrow(id))
            culinaryRepository.flush()
        }.onFailure { // it: Throwable ->
            when (it) {
                is DataIntegrityViolationException -> throw CulinaryInUseException()
                else -> throw it
            }
        }
    }
}
