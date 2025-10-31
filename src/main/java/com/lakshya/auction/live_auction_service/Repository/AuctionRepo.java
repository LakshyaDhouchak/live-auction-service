package com.lakshya.auction.live_auction_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lakshya.auction.live_auction_service.Entity.AuctionItem;

public interface AuctionRepo extends JpaRepository<AuctionItem,Long> {
    
}
