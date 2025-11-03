package com.lakshya.auction.live_auction_service.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

/**
 * Service class for handling JWT creation, validation, and claim extraction.
 * Renamed from AuthService to JwtService for better clarity.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    // Note: Removed 'final' because @Value is used for field injection.
    @Value("${application.security.jwt.secret-key}")
    private String secretKey; 
    
    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Generates a JWT token for the given user details.
     * @param userDetails The details of the user for whom the token is generated.
     * @return The generated JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        // Generates the token with the username as the subject, current time as issuedAt,
        // and the configured expiration time.
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from the JWT token.
     * @param token The JWT string.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the token using a Claims resolver function.
     * @param token The JWT string.
     * @param claimsResolver The function to apply to the claims.
     * @param <T> The type of the claim to be extracted.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT and extracts all claims.
     * @param token The JWT string.
     * @return The Claims object containing all token data.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retrieves the signing key from the base64 encoded secret key.
     * @return The signing SecretKey.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Checks if a token is valid for a given user.
     * @param token The JWT string.
     * @param userDetails The UserDetails object for validation.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the token has expired.
     * @param token The JWT string.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     * @param token The JWT string.
     * @return The Date object representing the token's expiration time.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}