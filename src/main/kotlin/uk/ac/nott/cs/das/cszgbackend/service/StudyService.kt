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
    fun getAllStudies(): Either<ResponseStatusException, Iterable<Study>>
    fun getStudy(id: UUID): Either<ResponseStatusException, Study>
    fun addStudy(study: Study): Either<ResponseStatusException, Study>
    fun getAllReports(): Either<ResponseStatusException, Iterable<Report>>
    fun getReportsForStudy(id: UUID): Either<ResponseStatusException, Iterable<Report>>
    fun getReport(id: UUID): Either<ResponseStatusException, Report>
    fun addReport(report: Report): Either<ResponseStatusException, Report>
    suspend fun associateStudyReport(studyId: UUID, reportId: UUID): Either<ResponseStatusException, Pair<UUID, UUID>>
}

@Service
class StudyServiceImpl(
    private val studyRepo: StudyRepository,
    private val reportRepo: ReportRepository
) : StudyService {

    override fun getAllStudies() = studyRepo.findAllFx()

    override fun getStudy(id: UUID) = studyRepo.findByIdFx(id)

    override fun addStudy(study: Study) = studyRepo.saveFx(study)

    override fun getAllReports() = reportRepo.findAllFx()

    override fun getReportsForStudy(id: UUID) = studyRepo.findByIdFx(id).map { it.reports }

    override fun getReport(id: UUID) = reportRepo.findByIdFx(id)

    override fun addReport(report: Report) = reportRepo.saveFx(report)

    override suspend fun associateStudyReport(studyId: UUID, reportId: UUID) =
        either<ResponseStatusException, Pair<UUID, UUID>> {
            var study = studyRepo.findByIdFx(studyId).bind()
            var report = reportRepo.findByIdFx(reportId).bind()

            study.reports.add(report)
            report.studies.add(study)
            study = studyRepo.saveFx(study).bind()
            report = reportRepo.saveFx(report).bind()
            Pair(study.id, report.id)
        }
}
