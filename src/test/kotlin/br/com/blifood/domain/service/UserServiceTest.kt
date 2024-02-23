package br.com.blifood.domain.service

import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.exception.UserAlreadyExistsException
import br.com.blifood.domain.exception.UserInvalidPasswordException
import br.com.blifood.domain.exception.UserNotAuthorizedException
import br.com.blifood.domain.exception.UserNotFoundException
import br.com.blifood.domain.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

class UserServiceTest : DescribeSpec({

    val userRepository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userService = UserService(userRepository, passwordEncoder)

    val user = createUser()
    val userId = user.id
    val userPassword = user.password
    val userEmail = user.email
    val encodedPassword = "encodedPassword"
    val userEncodedPass = user.copy(password = "encodedPassword")

    describe("save") {
        beforeEach { clearAllMocks() }

        it("should save user") {
            every { userRepository.findByEmail(any()) } returns null
            every { passwordEncoder.encode(any()) } returns encodedPassword
            every { userRepository.save(any()) } returns userEncodedPass

            val result = userService.save(user)

            result shouldBe userEncodedPass
            verify { userRepository.save(userEncodedPass) }
        }

        it("should throw exception when user already exists") {
            val newUser = createUser(id = 0)
            every { userRepository.findByEmail(any()) } returns user

            val exception = shouldThrow<UserAlreadyExistsException> { userService.save(newUser) }

            exception shouldBe UserAlreadyExistsException()
        }

        it("should encode password before saving") {
            every { userRepository.findByEmail(any()) } returns null
            every { passwordEncoder.encode(any()) } returns encodedPassword
            every { userRepository.save(any()) } returns userEncodedPass

            userService.save(user)

            verify { passwordEncoder.encode(encodedPassword) }
            verify { userRepository.save(userEncodedPass) }
        }

        it("should not encode password when it's blank") {
            val userEmptyPass = createUser(password = "")
            every { userRepository.findByEmail(any()) } returns null
            every { userRepository.save(any()) } returns userEmptyPass

            userService.save(userEmptyPass)

            verify(exactly = 0) { passwordEncoder.encode(any()) }
            verify { userRepository.save(userEmptyPass) }
        }
    }

    describe("changePassword") {
        val newPassword = userPassword

        it("should change user password") {
            every { userRepository.findById(any()) } returns Optional.of(user)
            every { passwordEncoder.matches(any(), any()) } returns true
            every { passwordEncoder.encode(any()) } returns encodedPassword
            every { userRepository.save(any()) } returns userEncodedPass

            userService.changePassword(userId, userPassword, newPassword)

            user.password shouldBe encodedPassword
            verify { passwordEncoder.encode(newPassword) }
            verify { userRepository.save(userEncodedPass) }
        }

        it("should throw UserInvalidPasswordException when current password is incorrect") {
            every { userRepository.findById(any()) } returns Optional.of(user)
            every { passwordEncoder.matches(any(), any()) } returns false

            val exception = shouldThrow<UserInvalidPasswordException> {
                userService.changePassword(userId, userPassword, newPassword)
            }

            exception shouldBe UserInvalidPasswordException()
            verify { userRepository.findById(userId) }
            verify { passwordEncoder.matches(userPassword, user.password) }
        }
    }

    describe("changeProfile") {
        val requesterId = 2L
        val requester = User(id = requesterId, email = "admin@example.com", profile = UserProfile.ADMIN)

        it("should change user profile when requester is an admin") {
            every { userRepository.findById(any()) } returns Optional.of(requester)
            every { userRepository.findById(any()) } returns Optional.of(user)
            every { userRepository.save(any()) } returns user

            userService.changeProfile(requesterId, userId, UserProfile.MANAGER)

            user.profile shouldBe UserProfile.MANAGER
            verify { userRepository.findById(requesterId) }
            verify { userRepository.findById(userId) }
            verify { userRepository.save(user) }
        }

        it("should throw UserNotAuthorizedException when requester is not an admin") {
            every { userRepository.findById(any()) } returns Optional.of(user)

            val exception = shouldThrow<UserNotAuthorizedException> {
                userService.changeProfile(requesterId, userId, UserProfile.MANAGER)
            }

            exception shouldBe UserNotAuthorizedException()
            verify { userRepository.findById(requesterId) }
        }
    }

    describe("findOrThrow") {
        it("should return user when found") {
            every { userRepository.findById(any()) } returns Optional.of(user)

            val result = userService.findOrThrow(userId)

            result shouldBe user
            verify { userRepository.findById(userId) }
        }

        it("should throw UserNotFoundException when user not found") {
            every { userRepository.findById(any()) } returns Optional.empty()

            val exception = shouldThrow<UserNotFoundException> {
                userService.findOrThrow(userId)
            }

            exception shouldBe UserNotFoundException()
            verify { userRepository.findById(userId) }
        }
    }

    describe("validateLogin") {
        it("should return user when login credentials are correct") {
            every { userRepository.findByEmail(any()) } returns user
            every { passwordEncoder.matches(any(), any()) } returns true

            val result = userService.validateLogin(userEmail, userPassword)

            result shouldBe user
            verify { userRepository.findByEmail(any()) }
            verify { passwordEncoder.matches(any(), any()) }
        }

        it("should throw UserInvalidPasswordException when password is incorrect") {
            every { userRepository.findByEmail(any()) } returns user
            every { passwordEncoder.matches(any(), any()) } returns false

            val exception = shouldThrow<UserInvalidPasswordException> {
                userService.validateLogin(userEmail, userPassword)
            }

            exception shouldBe UserInvalidPasswordException()
            verify { userRepository.findByEmail(userEmail) }
            verify { passwordEncoder.matches(userPassword, user.password) }
        }

        it("should throw UserNotFoundException when user is not found") {
            every { userRepository.findByEmail(any()) } returns null

            val exception = shouldThrow<UserNotFoundException> {
                userService.validateLogin(userEmail, userPassword)
            }

            exception shouldBe UserNotFoundException()
            verify { userRepository.findByEmail(userEmail) }
        }
    }
})
