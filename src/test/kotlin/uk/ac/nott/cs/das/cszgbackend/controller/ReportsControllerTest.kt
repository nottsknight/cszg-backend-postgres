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

import com.ninjasquad.springmockk.MockkBean
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import uk.ac.nott.cs.das.cszgbackend.CszgSecurityTestConfig
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService
import kotlin.test.assertNotNull

@ExtendWith(MockKExtension::class)
@WebMvcTest(controllers = [ReportsController::class])
@ContextConfiguration(classes = [CszgSecurityTestConfig::class])
@DisplayName("Given ReportsController")
class ReportsControllerTest {
    @MockkBean
    private lateinit var service: StudyReportService

    @Autowired
    private lateinit var controller: ReportsController

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
}
