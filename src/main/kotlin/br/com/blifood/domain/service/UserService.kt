package br.com.blifood.domain.service

import br.com.blifood.domain.entity.User
import br.com.blifood.domain.exception.UserAlreadyExistsException
import br.com.blifood.domain.exception.UserInvalidPasswordException
import br.com.blifood.domain.exception.UserNotFoundException
import br.com.blifood.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun save(user: User): User {
        userRepository.findByEmail(user.email)?.let {
            if (it.id != user.id) throw UserAlreadyExistsException()
        }
        /*if (user.isNew()) {

            user.password = passwordEncoder.encode(user.password)
        }*/
        return userRepository.save(user)
    }

    @Transactional
    fun changePassword(userId: Long, password: String, newPassword: String) {
        val user = findOrThrow(userId)
        // if (!passwordEncoder.matches(password, user.password) {
        if (password != user.password) {
            throw UserInvalidPasswordException()
        }
        // user.password = passwordEncoder.encode(user.password)
        user.password = newPassword
        userRepository.save(user)
    }

    fun findOrThrow(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException() }
    }
}
