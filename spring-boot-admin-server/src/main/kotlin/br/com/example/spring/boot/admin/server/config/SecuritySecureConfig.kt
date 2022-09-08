package br.com.example.spring.boot.admin.server.config

import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.UUID


@Configuration
class SecuritySecureConfig(private val adminServer: AdminServerProperties) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl(adminServer.path("/"))

        http.authorizeRequests()
            .antMatchers(this.adminServer.contextPath + "/assets/**").permitAll()
            .antMatchers(this.adminServer.contextPath + "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage(this.adminServer.contextPath + "/login")
            .successHandler(successHandler)
            .and()
            .logout()
            .logoutUrl(this.adminServer.contextPath + "/logout")
            .and()
            .httpBasic()
            .and()
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringRequestMatchers(
                AntPathRequestMatcher(
                    adminServer.contextPath +
                            "/instances", HttpMethod.POST.toString()
                ),
                AntPathRequestMatcher(
                    adminServer.contextPath +
                            "/instances/*", HttpMethod.DELETE.toString()
                ),
                AntPathRequestMatcher(adminServer.contextPath + "/actuator/**")
            )
            .and()
            .rememberMe()
            .key(UUID.randomUUID().toString())
            .tokenValiditySeconds(1209600)

        return http.build()
    }
}
