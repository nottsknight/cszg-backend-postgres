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
import org.springframework.http.HttpStatus
import uk.ac.nott.cs.das.cszgbackend.model.participant.*
import java.io.IOException
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
    @DisplayName("When getAllParticipants")
    inner class GetAllParticipants {
        @Nested
        @DisplayName("When the repository is functioning")
        inner class GoodRepo {
            @Test
            @DisplayName("Then an empty repo returns an empty iterable")
            fun emptyIterable() = runBlocking {
                every { participantRepo.findAll() } returns listOf()
                val ps = service.getAllParticipants()
                assertTrue { ps is Either.Right }
                ps as Either.Right
                assertEquals(0, ps.value.count())
            }

            @Test
            @DisplayName("Then a non-empty repo returns a non-empty iterable")
            fun nonEmptyIterable() = runBlocking {
                every { participantRepo.findAll() } returns listOf(dummyParticipant)
                val ps = service.getAllParticipants()
                assertTrue { ps is Either.Right }
                ps as Either.Right
                assertEquals(1, ps.value.count())
            }
        }

        @Nested
        @DisplayName("When the repository is not functioning")
        inner class BadRepo {
            @Test
            @DisplayName("Then the service returns a 500 error")
            fun brokenRepo() = runBlocking {
                every { participantRepo.findAll() } throws IOException()
                val ps = service.getAllParticipants()
                assertTrue { ps is Either.Left }
                ps as Either.Left
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ps.value.status)
            }
        }
    }

    @Nested
    @DisplayName("When getParticipant")
    inner class GetParticipant {
        @Nested
        @DisplayName("When the repository is functioning")
        inner class GoodRepo {
            @Test
            @DisplayName("Then an existing UUID returns the participant")
            fun existingUuid() = runBlocking {
                every { participantRepo.findById(any()) } returns Optional.of(dummyParticipant)
                val p = service.getParticipant(UUID.randomUUID())
                assertTrue { p is Either.Right }

                p as Either.Right
                assertEquals(dummyParticipant, p.value)
            }

            @Test
            @DisplayName("Then a non-existent UUID returns a 404 error")
            fun nonExistentUuid() = runBlocking {
                every { participantRepo.findById(any()) } returns Optional.empty()
                val p = service.getParticipant(UUID.randomUUID())
                assertTrue { p is Either.Left }

                p as Either.Left
                assertEquals(HttpStatus.NOT_FOUND, p.value.status)
            }
        }

        @Nested
        @DisplayName("When the repository is not functioning")
        inner class BadRepo {
            @Test
            @DisplayName("Then the service returns a 500 error")
            fun brokenRepo() = runBlocking {
                every { participantRepo.findById(any()) } throws IOException()
                val p = service.getParticipant(UUID.randomUUID())
                assertTrue { p is Either.Left }
                p as Either.Left
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, p.value.status)
            }
        }
    }
}
