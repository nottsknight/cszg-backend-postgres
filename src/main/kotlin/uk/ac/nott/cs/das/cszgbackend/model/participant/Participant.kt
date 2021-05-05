package uk.ac.nott.cs.das.cszgbackend.model.participant

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "participant")
data class Participant(
    @Id val id: UUID = UUID.randomUUID(),
    @Column(unique = true) var username: String,
    var age: Int? = null,
    var gender: Char? = null,
    var genderDescription: String? = null,
    var passwordHash: String? = null,
    var validFrom: Calendar? = null,
    var validTo: Calendar? = null,
    @ManyToMany
    @JoinTable(
        name = "participant_role",
        joinColumns = [JoinColumn(name = "participantId")],
        inverseJoinColumns = [JoinColumn(name = "roleName")]
    )
    var roles: MutableSet<ParticipantRole>,
    @OneToOne(mappedBy = "participant") var ati: ParticipantAti? = null,
    @OneToMany(mappedBy = "participant") var tlx: MutableSet<ParticipantTlx> = mutableSetOf(),
    @OneToMany(mappedBy = "participant") var trust: MutableSet<ParticipantTrust> = mutableSetOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return id == (other as Participant).id
    }

    override fun hashCode() = id.hashCode()
}

@Entity
@Table(name = "role")
data class ParticipantRole(
    @Id var roleName: String
)

data class ParticipantBioDto(
    var age: Int,
    var gender: Char,
    var genderDescription: String?
)

interface ParticipantRepository : CrudRepository<Participant, UUID> {
    fun findByUsername(username: String): Optional<Participant>
}
