/**
 * This file is part of the CSzG backend.
 *
 * The CSzG backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The CSzG backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see https://www.gnu.org/licenses/.
 */
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.CszgSecurityTestConfig
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [StudiesController::class])
@ContextConfiguration(classes = [CszgSecurityTestConfig::class])
@DisplayName("StudiesController")
class StudiesControllerTest {
    @MockkBean
    private lateinit var service: StudyReportService

    @Autowired
    private lateinit var controller: StudiesController

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var mockStudy: Study

    @BeforeEach
    fun setUp() {
        mockStudy = Study(title = "Test", reports = mutableSetOf())
    }

    @Nested
    @DisplayName("GET /studies")
    inner class GetStudies {
        @Test
        @WithMockUser("test")
        fun `should return a 200 response with the studies if all goes well`() {
            coEvery { service.getAllStudies() } returns Either.Right(listOf())
            mockMvc.perform(get("/studies")).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("[]")
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 500 response if the server fails to produce the studies`() {
            coEvery { service.getAllStudies() } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(get("/studies")).andExpect {
                status().isInternalServerError
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.getAllStudies() } returns Either.Right(listOf())
            mockMvc.perform(get("/studies")).andExpect { status().isUnauthorized }
        }
    }

    @Nested
    @DisplayName("GET /studies/{id}")
    inner class GetStudiesId {
        @Test
        @WithMockUser("test")
        fun `should return a 200 response with the study if the ID exists`() {
            coEvery { service.getStudy(mockStudy.id) } returns Either.Right(mockStudy)
            mockMvc.perform(get("/studies/${mockStudy.id}")).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("""{"id":"${mockStudy.id}","title":"Test","reports":[]}""")
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 404 response if the ID doesn't exist`() {
            coEvery { service.getStudy(any()) } returns Either.Left(ResponseStatusException(HttpStatus.NOT_FOUND))
            mockMvc.perform(get("/studies/${mockStudy.id}")).andExpect {
                status().isNotFound
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 500 response if the service fails to get the study`() {
            coEvery { service.getStudy(any()) } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(get("/studies/${mockStudy.id}")).andExpect {
                status().isInternalServerError
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.getStudy(mockStudy.id) } returns Either.Right(mockStudy)
            mockMvc.perform(get("/studies/${mockStudy.id}")).andExpect {
                status().isUnauthorized
            }
        }
    }

    @Nested
    @DisplayName("POST /studies")
    inner class PostStudies {
        @Test
        @WithMockUser("admin")
        fun `should return a 201 response with the new study if creation succeeded`() {
            coEvery { service.addStudy(any()) } answers { Either.Right(firstArg()) }
            mockMvc.perform(post("/studies", mockStudy)).andExpect {
                status().isCreated
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("""{"id":"${mockStudy.id}","title":"Test","reports":[]}""")
            }
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 500 response if the service fails to save the study`() {
            coEvery { service.addStudy(any()) } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(post("/studies", mockStudy)).andExpect {
                status().isInternalServerError
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.addStudy(any()) } answers { Either.Right(firstArg()) }
            mockMvc.perform(post("/studies", mockStudy)).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 403 response if the user is unauthorized`() {
            coEvery { service.addStudy(any()) } answers { Either.Right(firstArg()) }
            mockMvc.perform(post("/studies", mockStudy)).andExpect {
                status().isForbidden
            }
        }
    }

    @Nested
    @DisplayName("POST /studies/{studyId}/link/{reportId}")
    inner class PostStudiesLinkReports
}
