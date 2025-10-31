package com.lakshya.auction.live_auction_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lakshya.auction.live_auction_service.Entity.Bid;

public interface BidRepo extends JpaRepository<Bid , Long> {

    
}
