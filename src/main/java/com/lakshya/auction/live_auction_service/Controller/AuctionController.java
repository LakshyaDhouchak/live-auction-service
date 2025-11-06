package com.lakshya.auction.live_auction_service.Controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakshya.auction.live_auction_service.Repository.BidRepo;
import com.lakshya.auction.live_auction_service.Service.RedisService;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    private final RedisService redisService;
    private final BidRepo bidRepo;

    public AuctionController(RedisService redisService, BidRepo bidRepo) {
        this.redisService = redisService;
        this.bidRepo = bidRepo;
    }

    @GetMapping("/{auctionItemId}/current-bid")
    public ResponseEntity<BigDecimal> getCurrentBid(@PathVariable Long auctionItemId) {
        // 1️⃣ Try Redis first
        Optional<BigDecimal> redisBid = redisService.getAuction(auctionItemId);

        if (redisBid.isPresent()) {
            return ResponseEntity.ok(redisBid.get());
        }

        // 2️⃣ Fallback to DB if Redis is empty
        Optional<BigDecimal> dbBid = bidRepo.findHighestAmountByAuctionItem(auctionItemId);

        return ResponseEntity.ok(dbBid.orElse(BigDecimal.ZERO));
    }
}

