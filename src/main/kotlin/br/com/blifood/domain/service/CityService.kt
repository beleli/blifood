package br.com.blifood.domain.service

import br.com.blifood.domain.entity.City
import br.com.blifood.domain.exception.CityAlreadyExistsException
import br.com.blifood.domain.exception.CityInUseException
import br.com.blifood.domain.exception.CityNotFoundException
import br.com.blifood.domain.repository.CityRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CityService(
    private val cityRepository: CityRepository,
    private val stateService: StateService
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<City> {
        return cityRepository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    fun findOrThrow(id: Long): City {
        return cityRepository.findById(id).orElseThrow { CityNotFoundException() }
    }

    @Transactional
    fun save(city: City): City {
        city.state = stateService.findOrThrow(city.state.id)
        cityRepository.findByNameAndStateId(city.name, city.state.id)?.let {
            if (it.id != city.id) throw CityAlreadyExistsException()
        }

        return cityRepository.save(city)
    }

    @Transactional(rollbackFor = [CityInUseException::class])
    fun delete(id: Long) {
        runCatching {
            cityRepository.delete(findOrThrow(id))
            cityRepository.flush()
        }.onFailure {
            when (it) {
                is DataIntegrityViolationException -> throw CityInUseException()
                else -> throw it
            }
        }
    }
}
