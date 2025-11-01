package com.lakshya.auction.live_auction_service.Service;

import java.util.List;

import com.lakshya.auction.live_auction_service.DTO.AuctionItemResponseDTO;
import com.lakshya.auction.live_auction_service.DTO.AuctionItemUpdateDTO;
import com.lakshya.auction.live_auction_service.DTO.CreateAuctionItemDTO;

public interface AuctionItemService {
    // define the methord
    AuctionItemResponseDTO createAuctionItem(CreateAuctionItemDTO dto);
    AuctionItemResponseDTO findAuctionById(Long id);
    List<AuctionItemResponseDTO> findAllAuctionItem();
    AuctionItemResponseDTO updateAuctionItem(Long id,AuctionItemUpdateDTO dto);
    void deleteAuctionItem(Long id);

    
} 
