package com.lakshya.auction.live_auction_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lakshya.auction.live_auction_service.Security.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

/**
 * Main Spring Security Configuration class.
 * Configures password encoder, security filter chain, and authentication manager.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // CORRECTION: This dependency was missing and caused a runtime error. 
    // It is injected here via @RequiredArgsConstructor.
    private final JwtRequestFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // Define the PasswordEncoder bean for use in the authentication process.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the main security filter chain.
     * Disables CSRF, sets session management to stateless, defines authorization rules,
     * and adds the JwtRequestFilter before the standard authentication filter.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            // Disable CSRF protection as JWT is used and state is handled by the token
            .csrf(csrf -> csrf.disable())
            
            // Set session policy to STATELESS, meaning no session will be created or used 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow unauthenticated access to registration and login endpoints
                .requestMatchers("/api/users/register", "/api/auth/login").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            
            // Add the custom JWT filter before Spring's default authentication filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();    
    }

    /**
     * Exposes the AuthenticationManager bean, which is required by AuthController.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}