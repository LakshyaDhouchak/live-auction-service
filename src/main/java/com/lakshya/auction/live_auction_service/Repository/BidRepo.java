package com.lakshya.auction.live_auction_service.Repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lakshya.auction.live_auction_service.Entity.Bid;

public interface BidRepo extends JpaRepository<Bid , Long> {
    // define the methord
    @Query("SELECT MAX(b.amount) FROM Bid b WHERE b.auctionItem.id =: auctionItemId")
    Optional<BigDecimal> findHighestAmountByActionItem(@Param("auctionItemId") Long auctionItemId );

    Optional<Bid> findTopByAuctionItemIdOrderByAmountDescBidTimeAsc(Long auctionItemId);
    
}
