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
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [ReportsController::class])
@ContextConfiguration(classes = [CszgSecurityTestConfig::class])
@DisplayName("ReportsController")
class ReportsControllerTest {
    @MockkBean
    private lateinit var service: StudyReportService

    @Autowired
    private lateinit var controller: ReportsController

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var mockReport: Report

    @BeforeEach
    fun setUp() {
        mockReport =
            Report(title = "Test", pdfData = byteArrayOf(), studies = mutableSetOf(), sentences = mutableSetOf())
    }

    @Nested
    @DisplayName("GET /reports")
    inner class GetReports {
        @Test
        @WithMockUser("test")
        fun `should return a 200 response with the reports if all goes well`() {
            coEvery { service.getAllReports() } returns Either.Right(listOf())
            mockMvc.perform(get("/reports")).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("[]")
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.getAllReports() } returns Either.Right(listOf())
            mockMvc.perform(get("/reports")).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 500 response if the service fails`() {
            coEvery { service.getAllReports() } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(get("/reports")).andExpect {
                status().isInternalServerError
            }
        }
    }

    @Nested
    @DisplayName("GET /reports/{id}")
    inner class GetReportsId {
        @Test
        @WithMockUser("test")
        fun `should return a 200 response with the report if the ID exists`() {
            coEvery { service.getReport(mockReport.id) } returns Either.Right(mockReport)
            mockMvc.perform(get("/reports/${mockReport.id}")).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("""{"id":"${mockReport.id}","title":"Test","pdfData":"","sentences":[]}""")
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user is unauthenticated`() {
            coEvery { service.getReport(mockReport.id) } returns Either.Right(mockReport)
            mockMvc.perform(get("/reports/${mockReport.id}")).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 404 response if the ID doesn't exist`() {
            coEvery { service.getReport(mockReport.id) } returns Either.Left(ResponseStatusException(HttpStatus.NOT_FOUND))
            mockMvc.perform(get("/reports/${mockReport.id}")).andExpect {
                status().isNotFound
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 500 response if the service fails`() {
            coEvery { service.getReport(mockReport.id) } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(get("/reports/${mockReport.id}")).andExpect {
                status().isInternalServerError
            }
        }
    }

    @Nested
    @DisplayName("POST /reports")
    inner class PostReports {
        @Test
        @WithMockUser("admin")
        fun `should return a 201 response with the report if saving succeeds`() {
            coEvery { service.addReport(mockReport) } returns Either.Right(mockReport)
            mockMvc.perform(post("/reports", mockReport)).andExpect {
                status().isCreated
                content().contentType(MediaType.APPLICATION_JSON)
                content().json("""{"id":"${mockReport.id}","title":"Test","pdfData":"","sentences":[]}""")
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user isn't authenticated`() {
            coEvery { service.addReport(mockReport) } returns Either.Right(mockReport)
            mockMvc.perform(post("/reports", mockReport)).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 403 response if the user isn't authorized`() {
            coEvery { service.addReport(mockReport) } returns Either.Right(mockReport)
            mockMvc.perform(post("/reports", mockReport)).andExpect {
                status().isForbidden
            }
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 500 response if the service fails`() {
            coEvery { service.addReport(mockReport) } returns Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            mockMvc.perform(post("/reports", mockReport)).andExpect {
                status().isInternalServerError
            }
        }
    }

    @Nested
    @DisplayName("POST /reports/{reportId}/link/{studyId}")
    inner class PostReportsLinkStudy {
        private lateinit var mockStudy: Study
        private lateinit var uri: String

        @BeforeEach
        fun setUp() {
            mockStudy = Study(title = "Test", reports = mutableSetOf())
            uri = "/reports/${mockReport.id}/link/${mockStudy.id}"
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 200 response with the linked study and report if everything works`() {
            coEvery {
                service.associateStudyReport(
                    mockStudy.id,
                    mockReport.id
                )
            } returns Either.Right(mockStudy to mockReport)
            mockMvc.perform(post(uri)).andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
            }
        }

        @Test
        @WithMockUser("foo")
        fun `should return a 401 response if the user isn't authenticated`() {
            coEvery { service.associateStudyReport(any(), any()) } returns Either.Right(mockStudy to mockReport)
            mockMvc.perform(post("/reports", mockReport)).andExpect {
                status().isUnauthorized
            }
        }

        @Test
        @WithMockUser("test")
        fun `should return a 403 response if the user isn't authorized`() {
            coEvery { service.associateStudyReport(any(), any()) } returns Either.Right(mockStudy to mockReport)
            mockMvc.perform(post(uri)).andExpect {
                status().isForbidden
            }
        }

        @Test
        @WithMockUser("admin")
        fun `should return a 500 response if the service fails`() {
            coEvery { service.associateStudyReport(any(), any()) } returns Either.Left(
                ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
            mockMvc.perform(post(uri)).andExpect {
                status().isInternalServerError
            }
        }
    }
}
