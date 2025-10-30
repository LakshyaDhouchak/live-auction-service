package com.lakshya.auction.live_auction_service.DTO;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AuctionItemUpdateDTO {
    // define the properties
    private String name;
    private String description;

    @Min(value = 0 , message = "starting price cannot be negative")
    private BigDecimal startPrice;

    @Future(message = "Auction end time must be in the future")
    private Instant endTime;
    
    private String status;
}
