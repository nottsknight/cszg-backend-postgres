package uk.ac.nott.cs.das.cszgbackend

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@TestConfiguration
class CszgSecurityTestConfig {
    @Bean
    fun mockUserDetailsService() = UserDetailsService { username ->
        when (username) {
            "test" -> mockUser
            "admin" -> mockAdmin
            else -> throw UsernameNotFoundException(username)
        }
    }

    private val mockUser = object : UserDetails {
        override fun getAuthorities() = listOf(SimpleGrantedAuthority("USER"))

        override fun getPassword() = "\$2y\$12\$MVdoSfG6c0foKNTtE7yVieYMRiXjoOSYxw1GZpDTzI88F3oZN6uG6"

        override fun getUsername() = "test"

        override fun isAccountNonExpired() = true

        override fun isAccountNonLocked() = true

        override fun isCredentialsNonExpired() = true

        override fun isEnabled() = true
    }

    private val mockAdmin = object : UserDetails {
        override fun getAuthorities() = listOf(SimpleGrantedAuthority("USER"), SimpleGrantedAuthority("ADMIN"))

        override fun getPassword() = "\$2y\$12\$M8ETz4nlZ6HDiQzMKuMyhuE/BIF/TNqDnltqFbs.SnyNEqMue8jZK"

        override fun getUsername() = "admin"

        override fun isAccountNonExpired() = true

        override fun isAccountNonLocked() = true

        override fun isCredentialsNonExpired() = true

        override fun isEnabled() = true
    }
}
