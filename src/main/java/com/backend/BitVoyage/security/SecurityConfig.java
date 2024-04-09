package com.backend.BitVoyage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.backend.BitVoyage.domain.UserRole;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/security-login/info").authenticated()
                .requestMatchers("/security-login/admin/**").hasAuthority(UserRole.ADMIN.name())
                .anyRequest().permitAll())
            .formLogin(form -> form
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginPage("/security-login/login")
                .defaultSuccessUrl("/security-login")
                .failureUrl("/security-login/login"))
            .logout(logout -> logout
                .logoutUrl("/security-login/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/security-login/login")
                .defaultSuccessUrl("/security-login")
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                .userService(principalOauth2UserService)))
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .accessDeniedHandler(new AccessDeniedHandlerImpl()));

        return http.build();
    }
}
