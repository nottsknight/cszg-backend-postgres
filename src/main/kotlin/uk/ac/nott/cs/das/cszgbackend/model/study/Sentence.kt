package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Sentence(
    @Id var id: UUID,
    var content: String,
    var precisionLabels: IntArray,
    var recallLabels: IntArray,
    @ManyToOne var report: Report
)

interface SentenceRepository : CrudRepository<Sentence, UUID>
