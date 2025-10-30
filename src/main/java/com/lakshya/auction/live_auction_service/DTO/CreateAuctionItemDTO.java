package com.lakshya.auction.live_auction_service.DTO;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAuctionItemDTO {
    // define the properties
    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "starting price is required")
    @Min(value = 0, message="starting price cannot be negative")
    private BigDecimal startPrice;

    @NotNull(message = "Auction end time is required")
    @Future(message = "Auction end time must be in the future")
    private Instant endTime;

}
