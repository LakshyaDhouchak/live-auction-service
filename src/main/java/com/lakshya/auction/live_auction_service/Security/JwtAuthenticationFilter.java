package com.lakshya.auction.live_auction_service.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Use the interface
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // For setDetails
import org.springframework.stereotype.Component; // <-- MISSING: Annotation for Spring management
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor; // <-- Recommended: For constructor injection


@Component

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Use interface UserDetailsService


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 1. Check if the header exists and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
             // Pass the request along and exit early if no token is found
             filterChain.doFilter(request, response);
             return; 
        }

        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 2. If username is found and no authentication is currently set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // userDetailsService is your custom implementation (e.g., CustomUserDetailsService)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 3. Validate the token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // 4. Create Authentication token and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, // credentials should be null since we're using JWT
                        userDetails.getAuthorities()
                );
                
                // Set the details (Good practice: ties the token to the request source)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}