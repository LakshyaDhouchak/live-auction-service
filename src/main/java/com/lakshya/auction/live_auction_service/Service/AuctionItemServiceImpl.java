package com.lakshya.auction.live_auction_service.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lakshya.auction.live_auction_service.DTO.AuctionItemResponseDTO;
import com.lakshya.auction.live_auction_service.DTO.AuctionItemUpdateDTO;
import com.lakshya.auction.live_auction_service.DTO.CreateAuctionItemDTO;
import com.lakshya.auction.live_auction_service.Entity.AuctionItem;
import com.lakshya.auction.live_auction_service.Entity.AuctionItem.AuctionStatus;
import com.lakshya.auction.live_auction_service.ExceptionHandling.DataConflictException;
import com.lakshya.auction.live_auction_service.ExceptionHandling.ResourceNotFoundException;
import com.lakshya.auction.live_auction_service.Repository.AuctionRepo;
import com.lakshya.auction.live_auction_service.Repository.BidRepo;

@Service
public class AuctionItemServiceImpl implements AuctionItemService{
    // define the properties
    private final AuctionRepo repo;
    private final BidRepo bidRepo;

    // define the constructor
    public AuctionItemServiceImpl(AuctionRepo repo , BidRepo bidRepo){
        this.repo = repo;
        this.bidRepo = bidRepo;
    }

    // define the methord
    private AuctionItemResponseDTO mapToResponse(AuctionItem auctionItem){
        AuctionItemResponseDTO dto =  new AuctionItemResponseDTO();
        dto.setId(auctionItem.getId());
        dto.setName(auctionItem.getName());
        dto.setDescription(auctionItem.getDescription());
        dto.setCurrentBid(auctionItem.getCurrentBid());
        dto.setHighBidderUserId(auctionItem.getHighBidderUserId());
        dto.setEndTime(auctionItem.getEndTime());
        dto.setStartPrice(auctionItem.getStartPrice());
        dto.setStatus(auctionItem.getStatus().name());
        return dto;
    }

    @Override
    @Transactional
    public AuctionItemResponseDTO createAuctionItem(CreateAuctionItemDTO dto) {
        // define the condiion
        AuctionItem auctionItem = new AuctionItem();
    
        auctionItem.setName(dto.getName());
        auctionItem.setDescription(dto.getDescription());
        auctionItem.setStartPrice(dto.getStartPrice());
        auctionItem.setEndTime(dto.getEndTime());
        auctionItem.setStatus(AuctionStatus.ACTIVE); 
        auctionItem.setCurrentBid(dto.getStartPrice()); 
        
        AuctionItem savedItem = repo.save(auctionItem);
        return mapToResponse(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public AuctionItemResponseDTO findAuctionById(Long id) {
        return repo.findById(id).map(this::mapToResponse).orElseThrow(()-> new ResourceNotFoundException("Auction Item not found with id:"+ id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionItemResponseDTO> findAllAuctionItem() {
        return repo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuctionItemResponseDTO updateAuctionItem(Long id, AuctionItemUpdateDTO dto) {
        AuctionItem existingItem = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auction Item not found with id: " + id));

        if (bidRepo.countByAuctionItemId(id) > 0) {
            throw new DataConflictException("Cannot update item details (name, price, end time) after bidding has started.");
        }
        
        if (StringUtils.hasText(dto.getName())) {
            existingItem.setName(dto.getName());
        }
        
        if (StringUtils.hasText(dto.getDescription())) {
            existingItem.setDescription(dto.getDescription());
        }
        
        if (dto.getStartPrice() != null) {
            existingItem.setStartPrice(dto.getStartPrice());
            existingItem.setCurrentBid(dto.getStartPrice());
        }
        
        if (dto.getEndTime() != null) {
            if (dto.getEndTime().isBefore(Instant.now())) {
                 throw new DataConflictException("Cannot set auction end time in the past.");
            }
            existingItem.setEndTime(dto.getEndTime());
        }
        
        if (dto.getStatus()!= null) {
            existingItem.setStatus(dto.getStatus());
        }

        AuctionItem updatedItem = repo.save(existingItem);
        return mapToResponse(updatedItem);
    }

    @Override
    @Transactional
    public void deleteAuctionItem(Long id) {
        AuctionItem itemToDelete = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Auction Item not found with ID: " + id));
        if (bidRepo.countByAuctionItemId(id) > 0) {
            throw new DataConflictException("Cannot delete auction item with existing bids. Please update its status instead.");
        }
        repo.delete(itemToDelete);
    }
    
}
