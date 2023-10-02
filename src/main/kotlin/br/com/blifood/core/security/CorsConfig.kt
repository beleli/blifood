package br.com.blifood.core.security

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilterRegistrationBean(): FilterRegistrationBean<CorsFilter> {
        val config = CorsConfiguration()
        config.allowCredentials = false
        config.allowedOrigins = listOf("*")
        config.setAllowedMethods(listOf("*"))
        config.allowedHeaders = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        val bean = FilterRegistrationBean<CorsFilter>()
        bean.setFilter(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE

        return bean
    }
}
