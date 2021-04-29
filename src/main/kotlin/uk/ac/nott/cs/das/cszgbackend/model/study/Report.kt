package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "report")
data class Report(
    @Id var id: UUID = UUID.randomUUID(),
    var title: String,
    var pdfData: ByteArray,
    @ManyToMany(mappedBy = "reports")
    var studies: MutableSet<Study>,
    @OneToMany(mappedBy = "report")
    var sentences: MutableSet<Sentence>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Report).id
    }

    override fun hashCode() = id.hashCode()

    companion object {
        fun fromDto(dto: ReportDto): Report {
            val pdfData = Base64.getDecoder().decode(dto.pdfData)
            return if (dto.id == null) Report(
                title = dto.title,
                pdfData = pdfData,
                studies = mutableSetOf(),
                sentences = dto.sentences
            )
            else Report(dto.id!!, dto.title, pdfData, mutableSetOf(), dto.sentences)
        }
    }
}

data class ReportDto(
    var id: UUID?,
    var title: String,
    var pdfData: String,
    var sentences: MutableSet<Sentence>
) {

    companion object {
        fun fromDao(dao: Report): ReportDto {
            val pdfData = Base64.getEncoder().encodeToString(dao.pdfData)
            return ReportDto(dao.id, dao.title, pdfData, dao.sentences)
        }
    }
}

interface ReportRepository : CrudRepository<Report, UUID>
