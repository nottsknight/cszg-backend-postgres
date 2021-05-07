package uk.ac.nott.cs.das.cszgbackend.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import uk.ac.nott.cs.das.cszgbackend.CszgSecurityTestConfig
import uk.ac.nott.cs.das.cszgbackend.service.StudyReportService

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
}
