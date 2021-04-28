package uk.ac.nott.cs.das.cszgbackend.service

import arrow.core.Either
import arrow.core.computations.either
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uk.ac.nott.cs.das.cszgbackend.model.study.Report
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.Study
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import uk.ac.nott.cs.das.cszgbackend.modelx.findAllFx
import uk.ac.nott.cs.das.cszgbackend.modelx.findByIdFx
import uk.ac.nott.cs.das.cszgbackend.modelx.saveFx
import java.util.*

interface StudyService {
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
class StudyServiceImpl(
    private val studyRepo: StudyRepository,
    private val reportRepo: ReportRepository
) : StudyService {

    override suspend fun getAllStudies() = studyRepo.findAllFx()

    override suspend fun getStudy(id: UUID) = studyRepo.findByIdFx(id)

    override suspend fun addStudy(study: Study) = studyRepo.saveFx(study)

    override suspend fun getAllReports() = reportRepo.findAllFx()

    override suspend fun getReportsForStudy(id: UUID) = studyRepo.findByIdFx(id).map { it.reports }

    override suspend fun getReport(id: UUID) = reportRepo.findByIdFx(id)

    override suspend fun addReport(report: Report) = reportRepo.saveFx(report)

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
