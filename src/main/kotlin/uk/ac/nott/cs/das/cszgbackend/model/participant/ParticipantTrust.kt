package uk.ac.nott.cs.das.cszgbackend.model.participant

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "trust")
data class ParticipantTrust(
    @Id var id: UUID = UUID.randomUUID(),
    @ManyToOne var participant: Participant,
    var taskNo: Int,
    var response1: Int,
    var response2: Int,
    var response3: Int,
    var response4: Int,
    var response5: Int,
    var response6: Int,
    var response7: Int,
    var response8: Int,
    var response9: Int
)

interface ParticipantTrustRepository : CrudRepository<ParticipantTrust, UUID>
