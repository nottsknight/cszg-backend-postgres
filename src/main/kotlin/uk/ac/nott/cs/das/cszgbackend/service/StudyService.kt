package uk.ac.nott.cs.das.cszgbackend.service

import org.springframework.stereotype.Service
import uk.ac.nott.cs.das.cszgbackend.model.study.ReportRepository
import uk.ac.nott.cs.das.cszgbackend.model.study.StudyRepository
import uk.ac.nott.cs.das.cszgbackend.modelx.findAllFx
import uk.ac.nott.cs.das.cszgbackend.modelx.findByIdFx
import java.util.*

@Service
class StudyService(private val studyRepo: StudyRepository, private val reportRepo: ReportRepository) {
    fun getAllStudies() = studyRepo.findAllFx()

    fun getStudy(id: UUID) = studyRepo.findByIdFx(id)

    fun getAllReports() = reportRepo.findAllFx()

    fun getReportsForStudy(id: UUID) = studyRepo.findByIdFx(id).map { it.reports }

    fun getReport(id: UUID) = reportRepo.findByIdFx(id)
}