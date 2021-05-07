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
package uk.ac.nott.cs.das.cszgbackend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class CszgSecurityConfig : WebSecurityConfigurerAdapter() {
    private val entryPoint = CszgAuthenticationEntryPoint("CSzG")
    private val bcryptStrength = 12

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests {
                it.antMatchers(HttpMethod.POST, "/studies/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/participants/**").hasRole("ADMIN")
                    .antMatchers("/verify").permitAll()
                    .antMatchers("/**").authenticated()
            }
            .formLogin { it.disable() }
            .httpBasic {
                it.realmName("CSzG")
                it.authenticationEntryPoint(entryPoint)
            }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.`$2Y`, bcryptStrength)
}

class CszgAuthenticationEntryPoint(realmName: String) : BasicAuthenticationEntryPoint() {
    init {
        setRealmName(realmName)
    }

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.addHeader("WWW-Authenticate", "Basic realm=\"$realmName\"")
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.writer?.println("HTTP Status 401 - ${authException?.message}")
    }
}
