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
package uk.ac.nott.cs.das.cszgx

import arrow.core.Either
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun <T : Any, ID : Any> CrudRepository<T, ID>.findAllFx() =
    try {
        Either.Right(findAll())
    } catch (e: Exception) {
        Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e))
    }

fun <T : Any, ID : Any> CrudRepository<T, ID>.findByIdFx(id: ID) =
    try {
        val entity = findById(id)
        if (entity.isPresent) Either.Right(entity.get()) else Either.Left(ResponseStatusException(HttpStatus.NOT_FOUND))
    } catch (e: Exception) {
        Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e))
    }

fun <T : Any, ID : Any> CrudRepository<T, ID>.saveFx(entity: T) =
    try {
        Either.Right(save(entity))
    } catch (e: Exception) {
        Either.Left(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.localizedMessage, e))
    }
