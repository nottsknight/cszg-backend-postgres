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
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return id == (other as ParticipantTrust).id
    }

    override fun hashCode() = id.hashCode()
}

interface ParticipantTrustRepository : CrudRepository<ParticipantTrust, UUID>
