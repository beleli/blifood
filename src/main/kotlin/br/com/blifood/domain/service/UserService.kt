package br.com.blifood.domain.service

import br.com.blifood.domain.entity.User
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.exception.UserAlreadyExistsException
import br.com.blifood.domain.exception.UserInvalidPasswordException
import br.com.blifood.domain.exception.UserNotAuthorizedException
import br.com.blifood.domain.exception.UserNotFoundException
import br.com.blifood.domain.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun save(user: User): User {
        userRepository.findByEmail(user.email)?.let {
            if (it.id != user.id) throw UserAlreadyExistsException()
        }
        if (user.password.isNotBlank()) {
            user.password = passwordEncoder.encode(user.password)
        }

        return userRepository.save(user)
    }

    @Transactional
    fun changePassword(userId: Long, password: String, newPassword: String) {
        val user = findOrThrow(userId)
        if (!passwordEncoder.matches(password, user.password)) {
            throw UserInvalidPasswordException()
        }
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

    @Transactional
    fun changeProfile(requesterId: Long, userId: Long, profile: UserProfile) {
        val requester = findOrThrow(requesterId)
        if (requester.profile != UserProfile.ADMIN) {
            throw UserNotAuthorizedException()
        }

        val user = findOrThrow(userId)
        user.profile = profile
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun findOrThrow(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException() }
    }

    @Transactional(readOnly = true)
    fun validateLogin(email: String, password: String): User {
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(password, user.password)) throw UserInvalidPasswordException()
        return user
    }
}
