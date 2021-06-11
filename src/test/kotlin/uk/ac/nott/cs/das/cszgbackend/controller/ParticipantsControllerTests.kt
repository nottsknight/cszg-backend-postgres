package uk.ac.nott.cs.das.cszgbackend.controller

import arrow.core.Either
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.CszgSecurityTestConfig
import uk.ac.nott.cs.das.cszgbackend.model.participant.Participant
import uk.ac.nott.cs.das.cszgbackend.service.ParticipantService

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [ParticipantsController::class])
@ContextConfiguration(classes = [CszgSecurityTestConfig::class])
@DisplayName("ParticipantsController")
class ParticipantsControllerTests {
    @MockkBean
    private lateinit var service: ParticipantService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var participant: Participant

    @BeforeEach
    fun setUp() {
        participant = Participant(
            username = "test",
            passwordHash = "\$2y\$12\$MVdoSfG6c0foKNTtE7yVieYMRiXjoOSYxw1GZpDTzI88F3oZN6uG6"
        )
    }

    @Nested
    @DisplayName("GET /participants")
    inner class GetParticipants {
        @Test
        @WithMockUser("admin")
        fun `should return a 200 response with all the participants if all goes well`() {
            coEvery { service.getAllParticipants() } returns Either.Right(listOf())
            mockMvc.perform(get("/participants")).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("[]")
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.getAllParticipants() } returns Either.Right(listOf())
            mockMvc.perform(get("/participants")).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 403 response if the user is unauthorized`() {
            coEvery { service.getAllParticipants() } returns Either.Right(listOf())
            mockMvc.perform(get("/participants")).andExpect {
                status().isForbidden
            }
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 500 response if the service fails`() {
            coEvery { service.getAllParticipants() } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(get("/participants")).andExpect {
                status().isInternalServerError
            }
        }
    }

    @Nested
    @DisplayName("GET /participants/{id}")
    inner class GetParticipantId {
        @Test
        @WithMockUser("admin")
        fun `should return a 200 response with the participant if all goes well`() {
            coEvery { service.getParticipant(participant.id) } returns Either.Right(participant)
            mockMvc.perform(get("/participants/${participant.id}")).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("""{"id":"${participant.id}","username":"${participant.username}","ati":null,"tlx":[],"trust":[]}""")
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.getParticipant(participant.id) } returns Either.Right(participant)
            mockMvc.perform(get("/participants/${participant.id}")).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 403 response if the user is unauthorized`() {
            coEvery { service.getParticipant(participant.id) } returns Either.Right(participant)
            mockMvc.perform(get("/participants/${participant.id}")).andExpect {
                status().isForbidden
            }
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 404 response if the participant ID doesn't exist`() {
            coEvery { service.getParticipant(participant.id) } returns Either.Left(ResponseStatusException(HttpStatus.NOT_FOUND))
            mockMvc.perform(get("/participants/${participant.id}")).andExpect {
                status().isNotFound
            }
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 500 response if the service fails`() {
            coEvery { service.getParticipant(any()) } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(get("/participants")).andExpect {
                status().isInternalServerError
            }
        }
    }
}
