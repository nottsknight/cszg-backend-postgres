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
package uk.ac.nott.cs.das.cszgbackend.model.study

import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "study")
class Study(
    @Id var id: UUID = UUID.randomUUID(),
    var title: String,
    @ManyToMany
    @JoinTable(
        name = "study_report",
        joinColumns = [JoinColumn(name = "studyId")],
        inverseJoinColumns = [JoinColumn(name = "reportId")]
    )
    var reports: MutableSet<Report>
) {

    override fun toString() = "[Study: id=$id, title=$title]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Study).id
    }

    override fun hashCode() = id.hashCode()

    companion object {
        fun fromDto(dto: StudyDto) =
            if (dto.id == null) Study(title = dto.title, reports = mutableSetOf())
            else Study(dto.id!!, dto.title, mutableSetOf())
    }
}

data class StudyDto(
    var id: UUID?,
    var title: String
) {

    companion object {
        fun fromDao(dao: Study) = StudyDto(dao.id, dao.title)
    }
}

interface StudyRepository : CrudRepository<Study, UUID>
