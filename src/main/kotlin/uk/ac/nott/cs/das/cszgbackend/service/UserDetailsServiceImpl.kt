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
package uk.ac.nott.cs.das.cszgbackend.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantRepository
import uk.ac.nott.cs.das.cszgbackend.model.participant.ParticipantUserDetails

@Service
class UserDetailsServiceImpl(private val participantRepo: ParticipantRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        participantRepo.findByUsername(username)
            .map { ParticipantUserDetails(it) }
            .orElseThrow { UsernameNotFoundException(username) }
}
