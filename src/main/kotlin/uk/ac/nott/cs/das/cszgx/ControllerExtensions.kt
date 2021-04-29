package uk.ac.nott.cs.das.cszgx

import arrow.core.Either

fun <A : Throwable, B> Either<A, B>.returnOrThrow() = when (this) {
    is Either.Left -> throw this.value
    is Either.Right -> this.value
}
