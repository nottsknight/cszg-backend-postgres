package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "study")
data class Study(
    @Id var id: UUID = UUID.randomUUID(),
    var title: String,
    @ManyToMany
    @JoinTable(
        name = "study_report",
        joinColumns = [JoinColumn(name = "studyId")],
        inverseJoinColumns = [JoinColumn(name = "reportId")]
    )
    var reports: MutableSet<Report>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Study).id
    }

    override fun hashCode() = id.hashCode()

    companion object {
        fun fromDto(dto: StudyDto) =
            if (dto.id == null) Study(title=dto.title, reports=mutableSetOf())
            else Study(dto.id!!, dto.title, mutableSetOf())
    }
}

data class StudyDto(
    var id: UUID?,
    var title: String
) {

    companion object {
        fun fromDao(dao: Study) = StudyDto(dao.id, dao.title)
    }
}

interface StudyRepository : CrudRepository<Study, UUID>
