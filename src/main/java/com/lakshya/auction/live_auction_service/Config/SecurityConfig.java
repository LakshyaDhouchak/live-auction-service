package com.lakshya.auction.live_auction_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Corrected import to match your provided class name
import com.lakshya.auction.live_auction_service.Security.JwtRequestFilter; 

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // define the properties
    // Corrected to match your class name JwtRequestFilter
    private final JwtRequestFilter jwtRequestFilter; 
    private final UserDetailsService userDetailsService;
    
    // define the method
    @Bean
    public PasswordEncoder password(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(password());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config ) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Added /api/auth/** and /api/users/register to permitAll, including /ws/**
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/register", "/api/auth/**", "/ws/**").permitAll() 
            .anyRequest().authenticated()
        )
        .authenticationProvider(authenticationProvider())
        // Corrected filter property name
        .addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class); 

        return http.build();
    }
}
