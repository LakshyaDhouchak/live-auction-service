package com.lakshya.auction.live_auction_service.DTO;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;

@Data
public class BidResponseDTO {
    // define the properties
    private Long id;
    private Long auctionItemId;
    private Long bidderUserId;
    private BigDecimal  amount;
    private Instant bidTime;
    private boolean isSuccessful;
}
