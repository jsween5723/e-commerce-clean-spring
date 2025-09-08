package io.jsween.ecommerce.adaptor.`web-api`.filter

import io.jsween.ecommerce.adaptor.security.AuthorizationHeaderParser
import io.jsween.ecommerce.domain.auth.Authentication
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(val authorizationHeaderParser: AuthorizationHeaderParser) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val header = request.getHeader(AUTHORIZATION)
        if (header == null) {
            request.setAttribute("authentication", Authentication.anonymous())
        } else {
            authorizationHeaderParser.parse(header)
                .onFailure {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                    return
                }.onSuccess {
                    request.setAttribute("authentication", it)
                }
        }
        filterChain.doFilter(request, response)
    }
}