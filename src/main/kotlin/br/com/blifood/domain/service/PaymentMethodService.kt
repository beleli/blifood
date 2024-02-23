package br.com.blifood.domain.service

import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.exception.PaymentMethodAlreadyExistsException
import br.com.blifood.domain.exception.PaymentMethodInUseException
import br.com.blifood.domain.exception.PaymentTypeNotFoundException
import br.com.blifood.domain.repository.PaymentMethodRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentMethodService(
    private val paymentMethodRepository: PaymentMethodRepository
) {
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<PaymentMethod> {
        return paymentMethodRepository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    fun findOrThrow(id: Long): PaymentMethod {
        return paymentMethodRepository.findById(id).orElseThrow { PaymentTypeNotFoundException() }
    }

    @Transactional
    fun save(paymentMethod: PaymentMethod): PaymentMethod {
        paymentMethodRepository.findByDescription(paymentMethod.description)?.let {
            if (it.id != paymentMethod.id) throw PaymentMethodAlreadyExistsException()
        }
        return paymentMethodRepository.save(paymentMethod)
    }

    @Transactional(rollbackFor = [PaymentMethodInUseException::class])
    fun delete(id: Long) {
        runCatching {
            paymentMethodRepository.delete(findOrThrow(id))
            paymentMethodRepository.flush()
        }.onFailure {
            when (it) {
                is DataIntegrityViolationException -> throw PaymentMethodInUseException()
                else -> throw it
            }
        }
    }
}
