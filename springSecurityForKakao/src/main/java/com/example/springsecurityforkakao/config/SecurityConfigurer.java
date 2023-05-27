package com.example.springsecurityforkakao.config;

import com.example.springsecurityforkakao.repository.UserRepository;
import com.example.springsecurityforkakao.service.PrincipalOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    private final PrincipalOauth2UserService principalOauth2UserService;

    public SecurityConfigurer(PrincipalOauth2UserService principalOauth2UserService) {
        this.principalOauth2UserService = principalOauth2UserService;
    }

    @Bean
    public SecurityFilterChain configurer(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizationManager ->
                        authorizationManager
                                .anyRequest()
                                .permitAll()
                );

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                        .csrf(AbstractHttpConfigurer::disable);

        http
                .oauth2Login(oauthLogin ->
                    oauthLogin
                            .loginPage("/loginForm")
                            .defaultSuccessUrl("/")
                            .failureUrl("/fail")
                            .userInfoEndpoint(info ->
                                    info.userService(principalOauth2UserService))
                    );

        return http.build();
    }
}
