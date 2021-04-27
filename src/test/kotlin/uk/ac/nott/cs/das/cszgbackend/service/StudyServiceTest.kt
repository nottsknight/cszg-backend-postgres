package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import uk.ac.nott.cs.das.cszgbackend.modelx.findAllFx

@ExtendWith(MockKExtension::class)
@DisplayName("Given StudyService")
class StudyServiceTest {
    @MockK
    private lateinit var studyRepo: StudyRepository

    @MockK
    private lateinit var reportRepo: ReportRepository

    private lateinit var service: StudyService

    @BeforeEach
    fun setUp() {
        service = StudyService(studyRepo, reportRepo)
    }

    @Nested
    @DisplayName("When repo has data")
    inner class StudyHasData {

        @Test
        @DisplayName("Then getAllStudies returns all the studies")
        fun getAllStudiesGood() {
            every { studyRepo.findAll() } returns mutableListOf()
            val studies = service.getAllStudies()
            Assertions.assertEquals(Either.Right::class, studies::class)
        }
    }
}