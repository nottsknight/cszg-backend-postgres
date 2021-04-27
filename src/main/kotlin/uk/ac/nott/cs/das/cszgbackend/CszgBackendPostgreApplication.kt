package uk.ac.nott.cs.das.cszgbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CszgBackendPostgreApplication

fun main(args: Array<String>) {
	runApplication<CszgBackendPostgreApplication>(*args)
}
