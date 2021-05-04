package uk.ac.nott.cs.das.cszgbackend

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class CszgSecurityConfig : WebSecurityConfigurerAdapter() {
    private val entryPoint = CszgAuthenticationEntryPoint()

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests {
                it.antMatchers(HttpMethod.POST, "/studies/**").hasRole("ADMIN")
                it.antMatchers(HttpMethod.GET, "/participants/**").hasRole("ADMIN")
                it.antMatchers("/verify").permitAll()
                it.antMatchers("/**").authenticated()
            }
            .formLogin { it.disable() }
            .httpBasic {
                it.realmName("CSzG")
                it.authenticationEntryPoint(entryPoint)
            }
    }
}

class CszgAuthenticationEntryPoint : BasicAuthenticationEntryPoint() {
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