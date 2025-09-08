package io.jsween.ecommerce.domain.auth

import java.util.*

data class Authentication(val id: UUID, val roles: Set<Role>) {
    companion object {
        fun anonymous(): Authentication {
            return Authentication(id = UUID.randomUUID(), roles = setOf(Role.ANONYMOUS))
        }
    }
}