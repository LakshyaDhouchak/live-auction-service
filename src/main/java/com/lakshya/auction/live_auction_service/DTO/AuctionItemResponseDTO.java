package com.lakshya.auction.live_auction_service.DTO;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;

@Data
public class AuctionItemResponseDTO {
    // define the properties

    private Long id;
    private String name;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentBid;
    private Long highBidderUserId;
    private Instant endTime;
    private String status;
}
