package uk.ac.nott.cs.das.cszgbackend.model.participant

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tlx")
data class ParticipantTlx(
    @Id var id: UUID = UUID.randomUUID(),
    @ManyToOne var participant: Participant,
    var taskNo: Int,
    var mentalDemand: Int,
    var physicalDemand: Int,
    var temporalDemand: Int,
    var effort: Int,
    var performance: Int,
    var frustration: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return id == (other as ParticipantTlx).id
    }

    override fun hashCode()= id.hashCode()
}

interface ParticipantTlxRepository : CrudRepository<ParticipantTlx, UUID>
