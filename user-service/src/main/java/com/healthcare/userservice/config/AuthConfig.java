package com.healthcare.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO Need To add url in DB or Properties
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/user/token", "/api/v1/user/patient/register", "/api/v1/user/admin/create", "/api/v1/user/doctor/create",
                        "/api/v1/user/doctor/all", "/api/v1/user/doctor/count", "/api/v1/user/admin/count", "/api/v1/user/patient/count",
                        "/api/v1/user/admin/{id}", "/api/v1/user/patient/{id}", "/api/v1/user/admin/all", "/api/v1/user/patient/all",
                        "/api/v1/user/admin/update", "/api/v1/user/doctor/{id}", "/api/v1/user/blood-group-options",
                        "/api/v1/user/designation-options", "/api/v1/user/department-options", "/api/v1/user/gender-options", "/v3/api-docs/**", "/swagger-ui/**",
                        "/api/v1/user/designation-options", "/api/v1/user/department-options", "/api/v1/user/gender-options",
                        "/api/v1/user/check-mobile", "/api/v1/user/admin/temp/request", "/api/v1/user/admin/tempdata",
                        "/api/v1/user/pending-admin-count", "/api/v1/user/pending-doctor-count", "/api/v1/user/pending-appointment-count",
                        "/api/v1/user/admin/request/check").permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
