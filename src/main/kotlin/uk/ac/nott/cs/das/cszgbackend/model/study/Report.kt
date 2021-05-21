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
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "report")
class Report(
    @Id var id: UUID = UUID.randomUUID(),
    var title: String,
    var pdfData: ByteArray,
    @ManyToMany(mappedBy = "reports")
    var studies: MutableSet<Study>,
    @OneToMany(mappedBy = "report")
    var sentences: MutableSet<Sentence>
) {

    override fun toString() = "[Report id=$id, title=$title]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Report).id
    }

    override fun hashCode() = id.hashCode()

    companion object {
        fun fromDto(dto: ReportDto): Report {
            val pdfData = Base64.getDecoder().decode(dto.pdfData)
            return if (dto.id == null) Report(
                title = dto.title,
                pdfData = pdfData,
                studies = mutableSetOf(),
                sentences = dto.sentences
            )
            else Report(dto.id!!, dto.title, pdfData, mutableSetOf(), dto.sentences)
        }
    }
}

data class ReportDto(
    var id: UUID?,
    var title: String,
    var pdfData: String,
    var sentences: MutableSet<Sentence>
) {

    companion object {
        fun fromDao(dao: Report): ReportDto {
            val pdfData = Base64.getEncoder().encodeToString(dao.pdfData)
            return ReportDto(dao.id, dao.title, pdfData, dao.sentences)
        }
    }
}

interface ReportRepository : CrudRepository<Report, UUID>
