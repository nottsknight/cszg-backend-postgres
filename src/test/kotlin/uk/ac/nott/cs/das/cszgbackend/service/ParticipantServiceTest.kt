package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.ac.nott.cs.das.cszgbackend.model.participant.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
@DisplayName("Given ParticipantService")
class ParticipantServiceTest {
    @MockK
    private lateinit var participantRepo: ParticipantRepository

    @MockK
    private lateinit var atiRepo: ParticipantAtiRepository

    @MockK
    private lateinit var tlxRepo: ParticipantTlxRepository

    @MockK
    private lateinit var trustRepo: ParticipantTrustRepository

    private lateinit var service: ParticipantService
    private lateinit var dummyParticipant: Participant

    @BeforeEach
    fun setUp() {
        service = ParticipantServiceImpl(participantRepo, atiRepo, tlxRepo, trustRepo)
        dummyParticipant = Participant(username = "abcd")
    }

    @Nested
    @DisplayName("When ParticipantRepo has data")
    inner class ParticipantRepo {
        @Test
        @DisplayName("Then getAllParticipants should return the participants")
        fun getAllParticipants() = runBlocking {
            every { participantRepo.findAll() } returns listOf(dummyParticipant)
            val participants = service.getAllParticipants()
            assertTrue { participants is Either.Right }

            participants as Either.Right
            assertEquals(1, participants.value.count())
        }

        @Test
        @DisplayName("Then getParticipant should return the participant")
        fun getParticipant() = runBlocking {
            every { participantRepo.findById(dummyParticipant.id) } returns Optional.of(dummyParticipant)
            val participant = service.getParticipant(dummyParticipant.id)
            assertTrue { participant is Either.Right }

            participant as Either.Right
            assertEquals("abcd", participant.value.username)
        }
    }
}
