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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.CszgSecurityTestConfig
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService
import kotlin.test.assertNotNull


@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [StudiesController::class])
@ContextConfiguration(classes = [CszgSecurityTestConfig::class])
@DisplayName("Given StudiesController")
class StudiesControllerTest {
    @MockkBean
    private lateinit var service: StudyReportService

    @Autowired
    private lateinit var controller: StudiesController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    @DisplayName("When the context is instantiated")
    inner class ContextLoad {
        @Test
        @DisplayName("Then the context should be created")
        fun testContextLoads() {
            assertNotNull(controller)
        }
    }

    @Nested
    @DisplayName("When GET /studies")
    @WithMockUser("test")
    inner class GetStudies {
        @Nested
        @DisplayName("When authenticated")
        inner class Authenticated {
            @Test
            @DisplayName("Then the controller returns the list of studies if the service works")
            fun getAllStudies() {
                coEvery { service.getAllStudies() } returns Either.Right(listOf())
                mockMvc.perform(get("/studies")).andExpect {
                    status().isOk
                    content().contentType("application/json")
                    content().json("[]")
                }
            }

            @Test
            @DisplayName("Then the controller returns a 500 error if the service breaks")
            fun getAllStudiesBad() {
                coEvery { service.getAllStudies() } returns
                        Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))

                mockMvc.perform(get("/studies")).andExpect {
                    status().isInternalServerError
                }
            }
        }

        @Nested
        @DisplayName("When not authenticated")
        @WithMockUser("foo")
        inner class NotAuthenticated {
            @Test
            @DisplayName("Then the controller returns a 401 error")
            fun getAllStudies() {
                coEvery { service.getAllStudies() } returns Either.Right(listOf())
                mockMvc.perform(get("/studies")).andExpect {
                    status().isUnauthorized
                }
            }
        }
    }
}
