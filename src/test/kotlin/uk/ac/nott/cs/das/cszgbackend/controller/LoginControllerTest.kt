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
package uk.ac.nott.cs.das.cszgbackend.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import uk.ac.nott.cs.das.cszgbackend.CszgSecurityTestConfig

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [LoginController::class])
@ContextConfiguration(classes = [CszgSecurityTestConfig::class])
@DisplayName("LoginController")
class LoginControllerTest {
    @Autowired
    private lateinit var controller: LoginController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser("test")
    fun `should return 204 if authentication succeeds`() {
        mockMvc.perform(get("/login")).andExpect {
            status().isNoContent
        }
    }

    @Test
    @WithMockUser("foo")
    fun `should return 401 if authentication fails`() {
        mockMvc.perform(get("/login")).andExpect {
            status().isUnauthorized
        }
    }
}
