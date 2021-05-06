package uk.ac.nott.cs.das.cszgx

fun <T> List<T>.pairs() = sequence {
    for (i in 1..lastIndex) yield(this@pairs[i - 1] to this@pairs[i])
}

fun <T> List<T>.splitOnIndices(predicate: (Int) -> Boolean) =
    (0..lastIndex+1).filter(predicate).pairs().map { (i,j) -> subList(i, j) }.toList()
