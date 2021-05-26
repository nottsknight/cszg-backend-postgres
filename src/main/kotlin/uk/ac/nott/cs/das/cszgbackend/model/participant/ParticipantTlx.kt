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
@Table(name = "tlx")
class ParticipantTlx(
    @Id var id: UUID = UUID.randomUUID(),
    @ManyToOne var participant: Participant? = null,
    var taskNo: Int,
    var mentalDemand: Int,
    var physicalDemand: Int,
    var temporalDemand: Int,
    var effort: Int,
    var performance: Int,
    var frustration: Int
) {

    override fun toString() =
        "[ParticipantTlx MD=$mentalDemand, PD=$physicalDemand, TD=$temporalDemand, EF=$effort, PF=$performance, FR=$frustration]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return id == (other as ParticipantTlx).id
    }

    override fun hashCode() = id.hashCode()
}

interface ParticipantTlxRepository : CrudRepository<ParticipantTlx, UUID>
