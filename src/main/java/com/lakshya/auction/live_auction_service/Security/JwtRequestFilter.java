package com.lakshya.auction.live_auction_service.Security;

import org.springframework.stereotype.Component; 
import org.springframework.web.filter.OncePerRequestFilter; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.context.SecurityContextHolder; 
import lombok.RequiredArgsConstructor; 

// IMPORTANT: Updated imports from javax.servlet to jakarta.servlet for Spring Boot 3+ compatibility.
import jakarta.servlet.FilterChain; 
import jakarta.servlet.ServletException; 
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse; 
import java.io.IOException; 

/**
 * Filter that executes once per request to check for a valid JWT in the Authorization header.
 * If a valid token is found, it sets the Authentication object in the SecurityContext.
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 1. Check if the header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtService.extractUsername(jwt);
        }

        // 2. If username is found and no authentication is currently set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 3. Validate the token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // 4. Create Authentication token and set it in the SecurityContext
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                // Set the details (optional, but good practice)
                // usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}