package com.lakshya.auction.live_auction_service.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication requests (login).
 * Inject dependencies for AuthenticationManager, JwtService, and CustomUserDetailService.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Handles user login by authenticating credentials and issuing a JWT.
     * @param request The LoginRequestDTO containing email and password.
     * @return A LoginResponseDTO containing the generated JWT and the user's email.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        
        // 1. Authenticate the user credentials using the AuthenticationManager.
        // This will check credentials against the UserDetailsService and PasswordEncoder.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // 2. Load UserDetails after successful authentication to generate the token.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        
        // 3. Generate JWT
        final String jwt = jwtService.generateToken(userDetails);
        
        // 4. Return the JWT to the client
        return ResponseEntity.ok(new LoginResponseDTO(jwt, userDetails.getUsername()));
    }
}
