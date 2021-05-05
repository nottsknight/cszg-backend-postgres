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
