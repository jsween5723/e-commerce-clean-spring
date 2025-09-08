package io.jsween.ecommerce.adaptor.`web-api`.resolver

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ResolverRegister : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        resolvers.add(AuthenticationArgumentResolver())
    }
}