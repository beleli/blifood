package br.com.blifood.domain.repository

import br.com.blifood.domain.createUser
import br.com.blifood.domain.entity.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataJpaTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private var savedUser: User = createUser()

    @BeforeEach
    fun setUp() {
        savedUser = userRepository.save(createUser())
    }

    @Test
    fun `should find user by email`() {
        val result = userRepository.findByEmail(savedUser.email)
        assertEquals(savedUser, result)
    }

    @Test
    fun `should return null if user not found by username`() {
        val result = userRepository.findByEmail("UnknownUser@mail.com")
        assertNull(result)
    }

    @Test
    fun `should insert a new user`() {
        val result = userRepository.findById(savedUser.id)
        assertEquals(savedUser, result.get())
    }

    @Test
    fun `should update an existing user`() {
        savedUser.name = "UpdatedUser"

        val updatedUser = userRepository.save(savedUser)

        val result = userRepository.findById(updatedUser.id)
        assertEquals("UpdatedUser", result.get().name)
    }

    @Test
    fun `should delete an existing user`() {
        userRepository.delete(savedUser)
        val result = userRepository.findById(savedUser.id)
        assertEquals(result.isEmpty, true)
    }
}
