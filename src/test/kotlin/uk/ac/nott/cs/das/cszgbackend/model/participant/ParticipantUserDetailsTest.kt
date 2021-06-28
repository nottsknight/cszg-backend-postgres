package uk.ac.nott.cs.das.cszgbackend.model.participant

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class ParticipantUserDetailsTest {
    private lateinit var participant: Participant
    private lateinit var userDetails: ParticipantUserDetails

    @BeforeEach
    fun setUp() {
        participant =
            Participant(username = "abcd", passwordHash = "dcba", roles = mutableSetOf(ParticipantRole("USER")))
        userDetails = ParticipantUserDetails(participant)
    }

    @Test
    @DisplayName("ParticipantUserDetails#username should match the participant")
    fun username() {
        userDetails.username.shouldBe(participant.username)
    }

    @Test
    @DisplayName("ParticipantUserDetails#password should match the participant")
    fun password() {
        userDetails.password.shouldBe(participant.passwordHash)
    }

    @Test
    @DisplayName("ParticipantUserDetails#authorities should match the roles of the participant")
    fun roles() {
        userDetails.authorities.shouldHaveSize(1)
        userDetails.authorities[0].authority.shouldBe("USER")
    }

    @Test
    @DisplayName("ParticipantUserDetails#isAccountNonLocked should be true")
    fun locked() {
        userDetails.isAccountNonLocked.shouldBeTrue()
    }

    @Nested
    @DisplayName("UserDetails#isEnabled")
    inner class Enabled {
        @Test
        @DisplayName("Should be true if validFrom date is null")
        fun validFromNull() {
            userDetails.isEnabled.shouldBeTrue()
        }

        @Test
        @DisplayName("Should be true if current time is after the valid from time")
        fun enabled() {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, -5)
            participant.validFrom = date
            userDetails.isEnabled.shouldBeTrue()
        }

        @Test
        @DisplayName("Should be false if current time is before the valid from time")
        fun notEnabled() {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, 5)
            participant.validFrom = date
            userDetails.isEnabled.shouldBeFalse()
        }
    }

    @Nested
    @DisplayName("ParticipantUserDetails#isAccountNonExpired")
    inner class AccountNonExpired {
        @Test
        @DisplayName("Should be true if validTo date is null")
        fun validToNull() {
            userDetails.isAccountNonExpired.shouldBeTrue()
        }

        @Test
        @DisplayName("Should be true if current time is before the valid to time")
        fun nonExpired() {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, 5)
            participant.validTo = date
            userDetails.isAccountNonExpired.shouldBeTrue()
        }

        @Test
        @DisplayName("Should be false if current time is after the valid from time")
        fun expired() {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, -5)
            participant.validTo = date
            userDetails.isAccountNonExpired.shouldBeFalse()
        }
    }

    @Nested
    @DisplayName("ParticipantUserDetails#isCredentialsNonExpired")
    inner class CredentialsNonExpired {
        @Test
        @DisplayName("Should be true if validTo date is null")
        fun validToNull() {
            userDetails.isCredentialsNonExpired.shouldBeTrue()
        }

        @Test
        @DisplayName("Should be true if current time is before the valid to time")
        fun nonExpired() {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, 5)
            participant.validTo = date
            userDetails.isCredentialsNonExpired.shouldBeTrue()
        }

        @Test
        @DisplayName("Should be false if current time is after the valid from time")
        fun expired() {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, -5)
            participant.validTo = date
            userDetails.isCredentialsNonExpired.shouldBeFalse()
        }
    }
}
