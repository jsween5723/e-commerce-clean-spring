package io.jsween.ecommerce.adaptor.`web-api`.resolver

import io.jsween.ecommerce.domain.auth.Authentication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.support.ModelAndViewContainer

class AuthenticationArgumentResolverTest {
    private val authenticationArgumentResolver = AuthenticationArgumentResolver()

    private inner class TestController() {
        fun method1(authentication: Authentication) {

        }
        fun method2(authentication: String) {}
    }

    @Test
    fun `파라미터가 authentication일 때 주입된다`() {
        //given
        val declaredMethod = TestController::class.java.getDeclaredMethod("method1", Authentication::class.java)
        val param = MethodParameter(declaredMethod, 0)
        val authentication = Authentication.anonymous()
        val request = MockHttpServletRequest().apply { setAttribute("authentication", authentication) }
        //when
        val actual = authenticationArgumentResolver.resolveArgument(
            param,
            ModelAndViewContainer(),
            ServletWebRequest(request),
            null
        )
        //then
        assertThat(actual).isEqualTo(authentication)
    }

    @Test
    fun `파라미터가 authentication이 아니면 통과하지 못한다`() {
        //given
        val declaredMethod = TestController::class.java.getDeclaredMethod("method2", String::class.java)
        val param = MethodParameter(declaredMethod, 0)
        //when
        //then
        assertThat ( authenticationArgumentResolver.supportsParameter(param) ).isFalse
    }
}