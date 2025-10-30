package com.lakshya.auction.live_auction_service.DTO;

import lombok.Data;

@Data
public class UserResponseDTO {
    // define the properties
    private Long id;
    private String userName;
    private String email;
    private String role;
}
