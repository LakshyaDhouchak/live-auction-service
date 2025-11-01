package com.lakshya.auction.live_auction_service.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lakshya.auction.live_auction_service.DTO.BidPlacementDTO;
import com.lakshya.auction.live_auction_service.DTO.BidResponseDTO;
import com.lakshya.auction.live_auction_service.Entity.AuctionItem;
import com.lakshya.auction.live_auction_service.Entity.Bid;
import com.lakshya.auction.live_auction_service.Entity.User;
import com.lakshya.auction.live_auction_service.ExceptionHandling.BidValidationException;
import com.lakshya.auction.live_auction_service.ExceptionHandling.DataConflictException;
import com.lakshya.auction.live_auction_service.ExceptionHandling.ResourceNotFoundException;
import com.lakshya.auction.live_auction_service.Repository.AuctionRepo;
import com.lakshya.auction.live_auction_service.Repository.BidRepo;
import com.lakshya.auction.live_auction_service.Repository.UserRepo;

@Service
public class BidServiceImpl implements BidService {
    // define the properties
    private final BidRepo repo;
    private final AuctionRepo auctionRepo;
    private final UserRepo userRepo;

    // define the constructor
    public BidServiceImpl(BidRepo repo , AuctionRepo auctionRepo , UserRepo userRepo){
        this.repo = repo;
        this.auctionRepo = auctionRepo;
        this.userRepo = userRepo;
    }

    // define the methord
    private BidResponseDTO mapToResponse(Bid bid){
        BidResponseDTO dto =  new BidResponseDTO();
        dto.setId(bid.getId());
        dto.setAmount(bid.getAmount());
        dto.setBidTime(bid.getBidTime());
        dto.setBidderUserId(bid.getBidder().getId());
        dto.setAuctionItemId(bid.getAuctionItem().getId());
        dto.setSuccessful(bid.isSuccessful());
        return dto;
    }

    @Override
    @Transactional
    public BidResponseDTO createBid(BidPlacementDTO dto) {
        // define the condition
        AuctionItem auctionItem = auctionRepo.findById(dto.getAuctionItemId()).orElseThrow(()-> new DataConflictException("AuctionItem not found by id: "+ dto.getAuctionItemId()));
        User user = userRepo.findById(dto.getBidderUserId()).orElseThrow(()-> new DataConflictException("User not Found with id: "+ dto.getBidderUserId()));
        if(auctionItem.getEndTime().isBefore(Instant.now())){
            throw new BidValidationException("Auction is already closed!!");
        }
        BigDecimal highestBid = repo.findHighestAmountByActionItem(dto.getAuctionItemId()).orElse(auctionItem.getStartPrice());

        if(dto.getAmount().compareTo(highestBid)<=0){
            throw new BidValidationException("Bid amount must be strictly higher than the current highest bid of: " + highestBid);
        }

        Bid createdBid = new Bid();
        createdBid.setAmount(highestBid);
        createdBid.setAuctionItem(auctionItem);
        createdBid.setBidder(user);
        createdBid.setBidTime(Instant.now());
        createdBid.setSuccessful(true);

        Bid savedBid = repo.save(createdBid);

        auctionItem.setHighBidderUserId(user.getId());
        auctionItem.setCurrentBid(createdBid.getAmount());
        auctionRepo.save(auctionItem);
        return mapToResponse(savedBid);

    }

    @Override
    @Transactional(readOnly = true)
    public BidResponseDTO findBidById(Long id) {
        return repo.findById(id).map(this::mapToResponse).orElseThrow(()-> new ResourceNotFoundException("Bid not found with id: "+ id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidResponseDTO> findAllBids() {
        return repo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBid(Long id) {
        // define the condition
        Bid bidToDelete =  repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Bid not found with id: "+ id));
        AuctionItem auctionItem = bidToDelete.getAuctionItem();

        if(bidToDelete.getBidTime().isBefore(Instant.now())){
            throw new BidValidationException("Auction is already closed !! so can't delete the bid");
        }
        boolean iscurrentlyHighestBid = bidToDelete.getAmount().compareTo(auctionItem.getCurrentBid()) == 0 && bidToDelete.getBidder().getId() == auctionItem.getHighBidderUserId();
        repo.delete(bidToDelete);

        if(iscurrentlyHighestBid){
            Optional<Bid> nextHighest = repo.findTopByAuctionItemIdOrderByAmountDescBidTimeAsc(auctionItem.getId());
            if (nextHighest.isPresent()) {
                Bid newHighest = nextHighest.get();
                auctionItem.setCurrentBid(newHighest.getAmount());
                auctionItem.setHighBidderUserId(newHighest.getBidder().getId());
            } else {
                auctionItem.setCurrentBid(auctionItem.getStartPrice());
                auctionItem.setHighBidderUserId(null); 
            }
            auctionRepo.save(auctionItem);    
        }
    }
    
}
