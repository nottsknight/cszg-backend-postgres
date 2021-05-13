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
import arrow.core.computations.either
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import uk.ac.nott.cs.das.cszgbackend.pdf.ReportPdfProcessor
import uk.ac.nott.cs.das.cszgx.findAllFx
import uk.ac.nott.cs.das.cszgx.findByIdFx
import uk.ac.nott.cs.das.cszgx.saveFx
import java.util.*

interface StudyReportService {
    suspend fun getAllStudies(): Either<ResponseStatusException, Iterable<Study>>
    suspend fun getStudy(id: UUID): Either<ResponseStatusException, Study>
    suspend fun addStudy(study: Study): Either<ResponseStatusException, Study>
    suspend fun getAllReports(): Either<ResponseStatusException, Iterable<Report>>
    suspend fun getReportsForStudy(id: UUID): Either<ResponseStatusException, Iterable<Report>>
    suspend fun getReport(id: UUID): Either<ResponseStatusException, Report>
    suspend fun addReport(report: Report): Either<ResponseStatusException, Report>
    suspend fun associateStudyReport(
        studyId: UUID,
        reportId: UUID
    ): Either<ResponseStatusException, Pair<Study, Report>>
}

@Service
class StudyReportServiceImpl(
    private val studyRepo: StudyRepository,
    private val reportRepo: ReportRepository
    private val pdfProcessor: ReportPdfProcessor
) : StudyReportService {

    override suspend fun getAllStudies() = studyRepo.findAllFx()

    override suspend fun getStudy(id: UUID) = studyRepo.findByIdFx(id)

    override suspend fun addStudy(study: Study) = studyRepo.saveFx(study)

    override suspend fun getAllReports() = reportRepo.findAllFx()

    override suspend fun getReportsForStudy(id: UUID) = studyRepo.findByIdFx(id).map { it.reports }

    override suspend fun getReport(id: UUID) = reportRepo.findByIdFx(id)

    override suspend fun addReport(report: Report) = either<ResponseStatusException, Report> {
        val processedReport = pdfProcessor.processReport(report).bind()
        reportRepo.saveFx(processedReport).bind()
    }

    override suspend fun associateStudyReport(studyId: UUID, reportId: UUID) =
        either<ResponseStatusException, Pair<Study, Report>> {
            var study = studyRepo.findByIdFx(studyId).bind()
            var report = reportRepo.findByIdFx(reportId).bind()

            study.reports.add(report)
            report.studies.add(study)
            study = studyRepo.saveFx(study).bind()
            report = reportRepo.saveFx(report).bind()
            Pair(study, report)
        }
}
