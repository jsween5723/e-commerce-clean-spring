package io.jsween.ecommerce.adaptor.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsween.ecommerce.domain.auth.Authentication
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderParser(val objectMapper: ObjectMapper) {
    fun parse(header: String): Result<Authentication> {
        return runCatching {
            objectMapper.readValue(header, Authentication::class.java)
        }
    }
}