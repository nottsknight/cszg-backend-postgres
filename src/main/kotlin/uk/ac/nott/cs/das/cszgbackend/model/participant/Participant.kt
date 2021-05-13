/**
 * This file is part of the CSzG backend.
 *
 * The CSzG backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The CSzG backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see https://www.gnu.org/licenses/.
 */
package uk.ac.nott.cs.das.cszgbackend.model.participant

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

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
    var roles: MutableSet<ParticipantRole> = mutableSetOf(),
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

data class ParticipantDto(
    val id: UUID,
    var username: String,
    var ati: ParticipantAti?,
    var tlx: Set<ParticipantTlx>,
    var trust: Set<ParticipantTrust>
) {

    companion object {
        fun fromDao(dao: Participant) = ParticipantDto(dao.id, dao.username, dao.ati, dao.tlx, dao.trust)
    }
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
