package uk.ac.nott.cs.das.cszgbackend.model.participant

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class ParticipantUserDetails(private val p: Participant) : UserDetails {
    override fun getAuthorities() = p.roles.map { SimpleGrantedAuthority(it.roleName) }

    override fun getPassword() = p.passwordHash

    override fun getUsername() = p.username

    override fun isAccountNonExpired() = Calendar.getInstance() <= p.validTo

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = Calendar.getInstance() <= p.validTo

    override fun isEnabled() = Calendar.getInstance() >= p.validFrom
}
