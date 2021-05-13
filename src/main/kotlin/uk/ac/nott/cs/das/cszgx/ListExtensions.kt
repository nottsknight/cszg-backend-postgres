package uk.ac.nott.cs.das.cszgx

fun <T> List<T>.pairs() = sequence {
    for (i in 1..lastIndex) yield(this@pairs[i - 1] to this@pairs[i])
}

fun <T> Iterable<T>.mode() = groupBy { it }.mapValues { it.value.size }.maxByOrNull { it.value }?.key
