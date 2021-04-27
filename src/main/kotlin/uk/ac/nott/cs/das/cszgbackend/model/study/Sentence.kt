package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class Sentence(
    @Id var id: UUID = UUID.randomUUID(),
    var content: String,
    var precisionLabels: IntArray?,
    var recallLabels: IntArray?,
    @ManyToOne var report: Report,
    @OneToMany(mappedBy = "sentence") var fragments: MutableSet<Fragment>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Sentence).id
    }

    override fun hashCode() = id.hashCode()
}

interface SentenceRepository : CrudRepository<Sentence, UUID>
