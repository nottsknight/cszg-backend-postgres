package uk.ac.nott.cs.das.cszgbackend.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun login() = Unit
}
