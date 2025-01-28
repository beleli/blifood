package br.com.blifood.api.security

import br.com.blifood.api.filter.JwtAuthenticationFilter
import br.com.blifood.core.properties.JwtKeyStoreProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtKeyStoreProperties::class)
class JwtResourceConfig(private val jwtKeyProvider: JwtKeyProvider) {

    private val whiteList = arrayOf(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/actuator/**",
        "/v1/login/**"
    )

    @Bean
    fun resourceServerFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors {}
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(*whiteList).permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtKeyProvider)
    }
}
