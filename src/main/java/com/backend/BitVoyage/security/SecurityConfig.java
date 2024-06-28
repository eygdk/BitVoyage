//package com.backend.BitVoyage.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandlerImpl;
//
//import com.backend.BitVoyage.domain.UserRole;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final PrincipalOauth2UserService principalOauth2UserService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/security-login/info").authenticated()
//                .requestMatchers("/security-login/admin/**").hasAuthority(UserRole.ADMIN.name())
//                .anyRequest().permitAll())
//            .formLogin(form -> form
//                .usernameParameter("loginId")
//                .passwordParameter("password")
//                .loginPage("/security-login/login")
//                .defaultSuccessUrl("/security-login")
//                .failureUrl("/security-login/login"))
//            .logout(logout -> logout
//                .logoutUrl("/security-login/logout")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID"))
//            .oauth2Login(oauth2 -> oauth2
//                .loginPage("/security-login/login")
//                .defaultSuccessUrl("/security-login")
//                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
//                .userService(principalOauth2UserService)))
//            .exceptionHandling(exceptions -> exceptions
//                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
//                .accessDeniedHandler(new AccessDeniedHandlerImpl()));
//
//        return http.build();
//    }
//}
package com.backend.BitVoyage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.backend.BitVoyage.domain.UserRole;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

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

//  아래 코드 전체가 2024.06.28에 새로 추가한 코드
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String id = defaultOAuth2User.getAttributes().get("id").toString();
            String body = """
                    {"id":"%s"}
                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        };
    }
}
