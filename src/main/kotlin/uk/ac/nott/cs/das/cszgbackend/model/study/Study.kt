package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.*

@Entity
data class Study(
    @Id var id: UUID,
    var title: String,
    @ManyToMany
    @JoinTable(
        name = "study_report",
        joinColumns = [JoinColumn(name = "studyId")],
        inverseJoinColumns = [JoinColumn(name = "reportId")]
    )
    var reports: MutableSet<Report>
)

interface StudyRepository : CrudRepository<Study, UUID>
