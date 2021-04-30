package uk.ac.nott.cs.das.cszgbackend.controller

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.ac.nott.cs.das.cszgbackend.service.StudyService

@ExtendWith(SpringExtension::class)
@WebMvcTest
@DisplayName("Given StudiesController")
class StudiesControllerTest {
    @MockkBean
    private lateinit var service: StudyService

    @Autowired
    private lateinit var controller: StudiesController
}