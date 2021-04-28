package uk.ac.nott.cs.das.cszgbackend.service

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

@Service
class StudyService(private val studyRepo: StudyRepository, private val reportRepo: ReportRepository) {
    fun getAllStudies() = studyRepo.findAllFx()

    fun getStudy(id: UUID) = studyRepo.findByIdFx(id)

    fun addStudy(study: Study) = studyRepo.saveFx(study)

    fun getAllReports() = reportRepo.findAllFx()

    fun getReportsForStudy(id: UUID) = studyRepo.findByIdFx(id).map { it.reports }

    fun getReport(id: UUID) = reportRepo.findByIdFx(id)

    fun addReport(report: Report) = reportRepo.saveFx(report)

    suspend fun associateStudyReport(studyId: UUID, reportId: UUID) =
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
