package uk.ac.nott.cs.das.cszgbackend.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import java.util.*

class UserDetailsServiceImplTests : DescribeSpec({
    describe("UserDetailsServiceImpl") {
        lateinit var repo: ParticipantRepository
        lateinit var service: UserDetailsServiceImpl

        beforeEach {
            repo = mockk()
            service = UserDetailsServiceImpl(repo)
        }

        it("should return the UserDetails if the username exists") {
            every { repo.findByUsername(any()) } answers { Optional.of(Participant(username = firstArg())) }
            val participant = service.loadUserByUsername("abcd")
            participant.username.shouldBe("abcd")
        }

        it("should throw UsernameNotFoundException if the username doesn't exist") {
            every { repo.findByUsername(any()) } returns Optional.empty()
            val ex = shouldThrow<UsernameNotFoundException> { service.loadUserByUsername("abcd") }
            ex.message.shouldBe("abcd")
        }
    }
})
