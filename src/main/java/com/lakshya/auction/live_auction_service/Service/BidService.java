package com.lakshya.auction.live_auction_service.Service;

import java.util.List;

import com.lakshya.auction.live_auction_service.DTO.BidPlacementDTO;
import com.lakshya.auction.live_auction_service.DTO.BidResponseDTO;

public interface BidService {
    // define the methord
    BidResponseDTO createBid(BidPlacementDTO dto);
    BidResponseDTO findBidById(Long id);
    List<BidResponseDTO> findAllBids();
    void deleteBid(Long id);

} 
