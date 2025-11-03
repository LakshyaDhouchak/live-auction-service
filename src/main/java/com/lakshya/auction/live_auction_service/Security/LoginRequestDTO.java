package com.lakshya.auction.live_auction_service.Security;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for login requests.
 * NOTE: Changed 'Email' to 'email' for standard Java naming convention.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor // Added AllArgsConstructor for convenience
public class LoginRequestDTO {
    
    // Changed to standard camelCase 'email'
    private String email; 
    private String password;
}
