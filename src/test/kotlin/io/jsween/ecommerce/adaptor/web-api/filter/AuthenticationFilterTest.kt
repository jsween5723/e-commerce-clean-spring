package io.jsween.ecommerce.adaptor.`web-api`.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsween.ecommerce.Integration
import io.jsween.ecommerce.domain.auth.Authentication
import io.jsween.ecommerce.domain.auth.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.util.*


@Integration
class AuthenticationFilterTest @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val sut: AuthenticationFilter
) {

    @Test
    fun `Authorization 헤더를 분석해 userId와 role을 취득한다`() {
        //given
        val authentication = Authentication(id = UUID.randomUUID(), roles = setOf(Role.USER))
        val request = MockHttpServletRequest().apply {
            addHeader("Authorization", objectMapper.writeValueAsString(authentication))
        }
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        //when
        sut.doFilter(request, response, filterChain)
        //then
        assertThat(request.getAttribute("authentication") as Authentication).isEqualTo(authentication)
    }

    @Test
    fun `Authorization 헤더 분석에 실패하면 401 status를 반환한다`() {
        //given
        val request = MockHttpServletRequest().apply {
            addHeader("Authorization", "wrong value")
        }
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        //when
        sut.doFilter(request, response, filterChain)
        //then
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `Authorization 헤더가 비어있으면 Anonymous Authentication이 할당된다`() {
        //given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        //when
        sut.doFilter(request, response, filterChain)
        //then
        val actual = request.getAttribute("authentication") as Authentication
        assertThat(actual.roles).isEqualTo(setOf(Role.ANONYMOUS))
    }
}