package uk.ac.nott.cs.das.cszgbackend.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import java.util.*

@ExtendWith(MockKExtension::class)
@DisplayName("UserDetailsServiceImpl")
class UserDetailsServiceImplTests {
    @MockK
    private lateinit var repo: ParticipantRepository

    private lateinit var service: UserDetailsServiceImpl

    @BeforeEach
    fun setUp() {
        service = UserDetailsServiceImpl(repo)
    }

    @Test
    @DisplayName("Should return UserDetails if the username exists")
    fun usernameExists() {
        every { repo.findByUsername(any()) } answers { Optional.of(Participant(username = firstArg())) }
        val participant = service.loadUserByUsername("abcd")
        participant.username.shouldBe("abcd")
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException if the username doesn't exist")
    fun usernameDoesNotExist() {
        every { repo.findByUsername(any()) } returns Optional.empty()
        val ex = shouldThrow<UsernameNotFoundException> { service.loadUserByUsername("abcd") }
        ex.message.shouldBe("abcd")
    }
}
