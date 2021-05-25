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
package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import uk.ac.nott.cs.das.cszgbackend.model.study.FragmentRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.SentenceRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import uk.ac.nott.cs.das.cszgbackend.pdf.ReportPdfProcessor
import java.io.IOException
import java.util.*

class StudyReportServiceTest : DescribeSpec({
    // mocks
    lateinit var studyRepo: StudyRepository
    lateinit var reportRepo: ReportRepository
    lateinit var sentenceRepo: SentenceRepository
    lateinit var fragmentRepo: FragmentRepository
    lateinit var pdfProcessor: ReportPdfProcessor

    // service
    lateinit var service: StudyReportService

    describe("StudyReportService") {
        beforeEach {
            studyRepo = mockk()
            reportRepo = mockk()
            sentenceRepo = mockk()
            fragmentRepo = mockk()
            pdfProcessor = mockk()
            service = StudyReportServiceImpl(studyRepo, reportRepo, sentenceRepo, fragmentRepo, pdfProcessor)
        }

        describe("#getAllStudies") {
            it("should return a Right value with the studies") {
                every { studyRepo.findAll() } returns listOf()
                val studies = service.getAllStudies()
                studies.shouldBeRight()
            }
        }

        describe("#getStudy") {
            it("should return the study if the ID exists") {
                val randomId = UUID.randomUUID()
                val study = Study(id = randomId, title = "Test", reports = mutableSetOf())
                every { studyRepo.findById(randomId) } returns Optional.of(study)

                val studyResult = service.getStudy(randomId)
                studyResult.shouldBeRight {
                    it.id.shouldBe(randomId)
                    it.title.shouldBe("Test")
                }
            }

            it("should return an exception if the ID doesn't exist") {
                every { studyRepo.findById(any()) } returns Optional.empty()

                val studyResult = service.getStudy(UUID.randomUUID())
                studyResult.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if the repo fails to read") {
                every { studyRepo.findById(any()) } throws IOException()

                val studyResult = service.getStudy(UUID.randomUUID())
                studyResult.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#addStudy") {
            lateinit var toSave: Study

            beforeEach {
                toSave = Study(title = "Test", reports = mutableSetOf())
            }

            it("should return the study if the save was successful") {
                coEvery { pdfProcessor.processReport(any()) } answers { Either.Right(firstArg()) }
                every { studyRepo.save(any()) } answers { firstArg() }

                val study = service.addStudy(toSave)
                study.shouldBeRight {
                    it.shouldBe(toSave)
                }
            }

            it("should return an exception if the save failed") {
                every { studyRepo.save(any()) } throws IOException()

                val result = service.addStudy(toSave)
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#getAllReports") {
            it("should return a Right value with the reports") {
                every { reportRepo.findAll() } returns listOf()
                val reports = service.getAllReports()
                reports.shouldBeRight()
            }
        }

        describe("#getReportsForStudy") {
            it("should return the reports if the study ID is found") {
                val report = Report(
                    title = "Test",
                    pdfData = byteArrayOf(),
                    studies = mutableSetOf(),
                    sentences = mutableSetOf()
                )
                val study = Study(title = "Test", reports = mutableSetOf(report))
                report.studies.add(study)

                every { studyRepo.findById(study.id) } returns Optional.of(study)
                val reportList = service.getReportsForStudy(study.id)
                reportList.shouldBeRight {
                    it.shouldHaveSize(1)
                    it.first().studies.shouldContain(study)
                }
            }

            it("should return an exception if the study ID is not found") {
                every { studyRepo.findById(any()) } returns Optional.empty()

                val reportList = service.getReportsForStudy(UUID.randomUUID())
                reportList.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if the repo fails to read") {
                every { studyRepo.findById(any()) } throws IOException()

                val reportList = service.getReportsForStudy(UUID.randomUUID())
                reportList.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#getReport") {
            it("should return the report if the ID exists") {
                val id = UUID.randomUUID()
                val report = Report(id, "Test", byteArrayOf(), mutableSetOf(), mutableSetOf())
                every { reportRepo.findById(id) } returns Optional.of(report)

                val reportResult = service.getReport(id)
                reportResult.shouldBeRight {
                    it.id.shouldBe(id)
                }
            }

            it("should return an exception if the ID doesn't exist") {
                every { reportRepo.findById(any()) } returns Optional.empty()

                val reportResult = service.getReport(UUID.randomUUID())
                reportResult.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if the repo failed to read") {
                every { reportRepo.findById(any()) } throws IOException()

                val reportResult = service.getReport(UUID.randomUUID())
                reportResult.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#addReport") {
            it("should return the report if saving succeeds") {
                val report = Report(
                    title = "Test",
                    pdfData = byteArrayOf(),
                    studies = mutableSetOf(),
                    sentences = mutableSetOf()
                )
                coEvery { pdfProcessor.processReport(any()) } answers { Either.Right(firstArg()) }
                every { reportRepo.save(report) } answers { firstArg() }

                val reportResult = service.addReport(report)
                reportResult.shouldBeRight {
                    it.shouldBe(report)
                }
            }

            it("should return an exception if saving fails") {
                val report = Report(
                    title = "Test",
                    pdfData = byteArrayOf(),
                    studies = mutableSetOf(),
                    sentences = mutableSetOf()
                )
                coEvery { pdfProcessor.processReport(any()) } answers { Either.Right(firstArg()) }
                every { reportRepo.save(report) } throws IOException()

                val reportResult = service.addReport(report)
                reportResult.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }

        describe("#associateStudyReport") {
            it("should return an exception if the study ID is not found") {
                every { studyRepo.findById(any()) } returns Optional.empty()

                val result = service.associateStudyReport(UUID.randomUUID(), UUID.randomUUID())
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return an exception if the report ID is not found") {
                every { studyRepo.findById(any()) } answers { Optional.of(Study(firstArg(), "Test", mutableSetOf())) }
                every { reportRepo.findById(any()) } returns Optional.empty()

                val result = service.associateStudyReport(UUID.randomUUID(), UUID.randomUUID())
                result.shouldBeLeft {
                    it.status.shouldBe(HttpStatus.NOT_FOUND)
                }
            }

            it("should return the report-study pair if both IDs are found") {
                val study = Study(title = "Test", reports = mutableSetOf())
                every { studyRepo.findById(study.id) } returns Optional.of(study)
                every { studyRepo.save(any()) } answers { firstArg() }
                val report = Report(
                    title = "Test",
                    pdfData = byteArrayOf(),
                    studies = mutableSetOf(),
                    sentences = mutableSetOf()
                )
                every { reportRepo.findById(report.id) } returns Optional.of(report)
                every { reportRepo.save(any()) } answers { firstArg() }

                val result = service.associateStudyReport(study.id, report.id)
                result.shouldBeRight {
                    it.first.shouldBe(study)
                    it.first.reports.shouldContain(it.second)
                    it.second.shouldBe(report)
                    it.second.studies.shouldContain(it.first)
                }
            }
        }
    }
})
