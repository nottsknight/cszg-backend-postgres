package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import java.io.IOException
import java.util.*
import kotlin.test.assertEquals

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
        service = StudyServiceImpl(studyRepo, reportRepo)
    }

    @Nested
    @DisplayName("Given StudyRepository")
    inner class StudyRepo {
        @Nested
        @DisplayName("When repo has data")
        inner class StudyHasData {
            @Test
            @DisplayName("Then getAllStudies returns all the studies")
            fun getAllStudies() {
                every { studyRepo.findAll() } returns mutableListOf(Study(title = "Test", reports = mutableSetOf()))
                val studies = service.getAllStudies()
                assertTrue { studies is Either.Right }

                studies as Either.Right
                assertEquals(1, studies.value.count())
            }

            @Nested
            @DisplayName("When existing UUID is given")
            inner class GoodUuid {
                @Test
                @DisplayName("Then getStudy returns the study")
                fun getStudy() {
                    val study = Study(title = "Test", reports = mutableSetOf())
                    every { studyRepo.findById(study.id) } returns Optional.of(study)

                    val retrievedStudy = service.getStudy(study.id)
                    assertTrue { retrievedStudy is Either.Right }

                    retrievedStudy as Either.Right
                    assertEquals("Test", retrievedStudy.value.title)
                    assertEquals(0, retrievedStudy.value.reports.size)
                }
            }

            @Nested
            @DisplayName("When non-existent UUID is given")
            inner class BadUuid {
                @Test
                @DisplayName("Then getStudy returns 404 error")
                fun getStudy() {
                    every { studyRepo.findById(any()) } returns Optional.empty()
                    val retrievedStudy = service.getStudy(UUID.randomUUID())
                    assertTrue { retrievedStudy is Either.Left }

                    retrievedStudy as Either.Left
                    assertEquals(HttpStatus.NOT_FOUND, retrievedStudy.value.status)
                }
            }

        }

        @Nested
        @DisplayName("When repo has no data")
        inner class StudyNoData {
            @Test
            @DisplayName("Then getAllStudies returns an empty iterable")
            fun getAllStudies() {
                every { studyRepo.findAll() } returns mutableListOf()
                val studies = service.getAllStudies()
                assertTrue { studies is Either.Right }

                studies as Either.Right
                assertEquals(0, studies.value.count())
            }

            @Test
            @DisplayName("Then getStudy returns a 404 error")
            fun getStudy() {
                every { studyRepo.findById(any()) } returns Optional.empty()
                val study = service.getStudy(UUID.randomUUID())
                assertTrue { study is Either.Left }

                study as Either.Left
                assertEquals(HttpStatus.NOT_FOUND, study.value.status)
            }
        }

        @Nested
        @DisplayName("When repo fails to read data")
        inner class RepoBroken {
            @Test
            @DisplayName("Then getAllStudies returns a 500 error")
            fun getAllStudies() {
                every { studyRepo.findAll() } throws IOException()
                val studies = service.getAllStudies()
                assertTrue { studies is Either.Left }

                studies as Either.Left
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, studies.value.status)
            }

            @Test
            @DisplayName("Then getStudy returns a 500 error")
            fun getStudy() {
                every { studyRepo.findById(any()) } throws IOException()
                val study = service.getStudy(UUID.randomUUID())
                assertTrue(study is Either.Left)

                study as Either.Left
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, study.value.status)
            }
        }
    }

    @Nested
    @DisplayName("Given ReportRepository")
    inner class ReportRepo {
        private val dummyReport = Report(
            title = "Test",
            pdfData = ByteArray(10) { i -> i.toByte() },
            studies = mutableSetOf(),
            sentences = mutableSetOf()
        )

        @Nested
        @DisplayName("When repo has data")
        inner class HasData {
            @Test
            @DisplayName("Then getAllReports should return the reports")
            fun getAllReports() {
                every { reportRepo.findAll() } returns listOf(dummyReport)
                val reports = service.getAllReports()
                assertTrue { reports is Either.Right }

                reports as Either.Right
                assertEquals(1, reports.value.count())
            }

            @Test
            @DisplayName("Then getReport should return the report")
            fun getReport() {
                every { reportRepo.findById(dummyReport.id) } returns Optional.of(dummyReport)
                val report = service.getReport(dummyReport.id)
                assertTrue { report is Either.Right }

                report as Either.Right
                assertEquals("Test", report.value.title)
                assertEquals(10, report.value.pdfData.size)
                assertEquals(0, report.value.studies.size)
                assertEquals(0, report.value.sentences.size)
            }
        }

        @Nested
        @DisplayName("When repo has no data")
        inner class NoData

        @Nested
        @DisplayName("When repo is broken")
        inner class RepoBroken
    }
}
