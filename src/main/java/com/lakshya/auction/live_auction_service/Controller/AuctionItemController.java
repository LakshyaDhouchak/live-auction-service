package com.lakshya.auction.live_auction_service.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakshya.auction.live_auction_service.DTO.AuctionItemResponseDTO;
import com.lakshya.auction.live_auction_service.DTO.AuctionItemUpdateDTO;
import com.lakshya.auction.live_auction_service.DTO.CreateAuctionItemDTO;
import com.lakshya.auction.live_auction_service.Service.AuctionItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auctions")
public class AuctionItemController {
    // define the properties
    private final AuctionItemService auctionItemService;

    // define the constructor
    public AuctionItemController(AuctionItemService auctionItemService){
        this.auctionItemService = auctionItemService;
    }

    // define the methord
    @PostMapping("/auction")
    public ResponseEntity<AuctionItemResponseDTO> createAuction(@Valid @RequestBody CreateAuctionItemDTO dto){
        AuctionItemResponseDTO auction = auctionItemService.createAuctionItem(dto);
        return new ResponseEntity<>(auction,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionItemResponseDTO> findAuctionById(@PathVariable Long id){
        AuctionItemResponseDTO auction = auctionItemService.findAuctionById(id);
        return ResponseEntity.ok(auction);
    }

    @GetMapping
    public ResponseEntity<List<AuctionItemResponseDTO>> findAllAuction(){
        List<AuctionItemResponseDTO> auctions = auctionItemService.findAllAuctionItem();
        return ResponseEntity.ok(auctions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionItemResponseDTO> updateAuction(@PathVariable Long id, @Valid @RequestBody AuctionItemUpdateDTO dto){
        AuctionItemResponseDTO updatedAuction = auctionItemService.updateAuctionItem(id, dto);
        return ResponseEntity.ok(updatedAuction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deteteAuction(@PathVariable Long id){
        auctionItemService.deleteAuctionItem(id);
        return ResponseEntity.noContent().build();
    }

}
