package uk.ac.nott.cs.das.cszgbackend.model.participant

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "participant")
data class Participant(
    @Id val id: UUID = UUID.randomUUID(),
    var username: String,
    var age: Int?,
    var gender: Char?,
    var genderDescription: String?,
    var passwordHash: String?,
    var validFrom: Calendar?,
    var validTo: Calendar?,
    @OneToOne(mappedBy = "participant") var ati: ParticipantAti?,
    @OneToMany(mappedBy = "participant") var tlx: MutableSet<ParticipantTlx>
)

interface ParticipantRepository : CrudRepository<Participant, UUID>
