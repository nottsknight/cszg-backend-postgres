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
