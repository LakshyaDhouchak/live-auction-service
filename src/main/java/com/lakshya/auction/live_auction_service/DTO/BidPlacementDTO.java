package com.lakshya.auction.live_auction_service.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BidPlacementDTO {
    // define the properties
    @NotNull(message = "Auction Item ID is required")
    private Long auctionItemId;

    @NotNull(message = "Bidder User ID is required") 
    private Long bidderUserId;

    @NotNull(message = "Bid anount is required")
    @DecimalMin(value = "0.01",inclusive = true ,message = "Bid ammount must be greater than zero")
    private BigDecimal amount;
}
