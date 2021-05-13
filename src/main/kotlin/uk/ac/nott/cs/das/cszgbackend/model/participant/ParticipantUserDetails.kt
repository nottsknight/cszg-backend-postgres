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
