package com.journal.journal_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.ignoringRequestMatchers("/work/**") // Disable CSRF for specific endpoints
//        ).authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).httpBasic();
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors()  // Enable CORS in Spring Security
                .and()
                .authorizeRequests()
                .dispatcherTypeMatchers(HttpMethod.valueOf("/work/**")).authenticated()  // Protect your endpoints
                .anyRequest().permitAll()  // Allow other endpoints
                .and()
                .httpBasic();  // Enable Basic Authentication
        return http.build();
    }
}
