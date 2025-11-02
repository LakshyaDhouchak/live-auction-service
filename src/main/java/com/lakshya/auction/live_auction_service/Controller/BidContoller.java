package com.lakshya.auction.live_auction_service.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakshya.auction.live_auction_service.DTO.BidPlacementDTO;
import com.lakshya.auction.live_auction_service.DTO.BidResponseDTO;
import com.lakshya.auction.live_auction_service.Service.BidService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bids")
public class BidContoller {
    // define the properties
    private final BidService bidService;
    
    // define the constructor
    public BidContoller(BidService bidService){
        this.bidService = bidService;
    }

    // define the methord
    @PostMapping("/bid")
    public ResponseEntity<BidResponseDTO> createBid(@Valid @RequestBody BidPlacementDTO dto){
        BidResponseDTO bid = bidService.createBid(dto);
        return new ResponseEntity<>(bid,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidResponseDTO> findBidById(@PathVariable Long id){
        BidResponseDTO bid = bidService.findBidById(id);
        return ResponseEntity.ok(bid);
    }

    @GetMapping
    public ResponseEntity<List<BidResponseDTO>> findAllBid(){
        List<BidResponseDTO> bids = bidService.findAllBids();
        return ResponseEntity.ok(bids);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteBidById(@PathVariable Long id){
        bidService.deleteBid(id);
        return ResponseEntity.noContent().build();
    }
}
