package com.lakshya.auction.live_auction_service.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lakshya.auction.live_auction_service.DTO.BidPlacementDTO;
import com.lakshya.auction.live_auction_service.DTO.BidResponseDTO;
import com.lakshya.auction.live_auction_service.Entity.AuctionItem;
import com.lakshya.auction.live_auction_service.Entity.Bid;
import com.lakshya.auction.live_auction_service.Entity.User;
import com.lakshya.auction.live_auction_service.ExceptionHandling.BidValidationException;
import com.lakshya.auction.live_auction_service.ExceptionHandling.ResourceNotFoundException;
import com.lakshya.auction.live_auction_service.Repository.AuctionRepo;
import com.lakshya.auction.live_auction_service.Repository.BidRepo;
import com.lakshya.auction.live_auction_service.Repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BidServiceImpl implements BidService {

    private final BidRepo repo;
    private final AuctionRepo auctionRepo;
    private final UserRepo userRepo;
    private final RedisService redisService;
    private final SimpMessagingTemplate messageTemplate;

    public BidServiceImpl(BidRepo repo, AuctionRepo auctionRepo, UserRepo userRepo,
                          RedisService redisService, SimpMessagingTemplate messageTemplate) {
        this.repo = repo;
        this.auctionRepo = auctionRepo;
        this.userRepo = userRepo;
        this.redisService = redisService;
        this.messageTemplate = messageTemplate;
    }

    // ✅ Helper: Map Bid → Response DTO
    private BidResponseDTO mapToResponse(Bid bid) {
        BidResponseDTO dto = new BidResponseDTO();
        dto.setId(bid.getId());
        dto.setAmount(bid.getAmount());
        dto.setBidTime(bid.getBidTime());
        dto.setBidderUserId(bid.getBidder().getId());
        dto.setAuctionItemId(bid.getAuctionItem().getId());
        dto.setSuccessful(bid.isSuccessful());
        return dto;
    }

    // ✅ Create a new bid with concurrency safety
    @Override
    @Transactional
    public synchronized BidResponseDTO createBid(BidPlacementDTO dto) {
        // --- Fetch entities ---
        AuctionItem auctionItem = auctionRepo.findById(dto.getAuctionItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Auction item not found by id: " + dto.getAuctionItemId()));

        User user = userRepo.findById(dto.getBidderUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getBidderUserId()));

        // --- Validate auction time ---
        if (auctionItem.getEndTime().isBefore(Instant.now())) {
            throw new BidValidationException("Auction is already closed!!");
        }

        // --- Check highest bid ---
        BigDecimal highestBid = repo.findHighestAmountByAuctionItem(dto.getAuctionItemId())
                .orElse(auctionItem.getStartPrice());

        if (dto.getAmount().compareTo(highestBid) <= 0) {
            throw new BidValidationException("Bid amount must be strictly higher than the current highest bid of: " + highestBid);
        }

        // --- Create and save bid ---
        Bid createdBid = new Bid();
        createdBid.setAmount(dto.getAmount());
        createdBid.setAuctionItem(auctionItem);
        createdBid.setBidder(user);
        createdBid.setBidTime(Instant.now());
        createdBid.setSuccessful(true);

        Bid savedBid = repo.save(createdBid);

        // --- Update auction entity ---
        auctionItem.setHighBidderUserId(user.getId());
        auctionItem.setCurrentBid(savedBid.getAmount());
        auctionRepo.save(auctionItem);

        // --- Cache in Redis ---
        redisService.setAuction(auctionItem.getId(), savedBid.getAmount());

        // --- Broadcast via WebSocket ---
        String topic = "/topic/bids/" + auctionItem.getId();
        BidResponseDTO responseDTO = mapToResponse(savedBid);
        messageTemplate.convertAndSend(topic, responseDTO);

        log.info("Bid created: id={}, AuctionItem={}, User={}, Amount={}", savedBid.getId(), auctionItem.getId(), user.getId(), savedBid.getAmount());

        return responseDTO;
    }

    // ✅ Find bid by ID
    @Override
    @Transactional(readOnly = true)
    public BidResponseDTO findBidById(Long id) {
        return repo.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + id));
    }

    // ✅ Get all bids
    @Override
    @Transactional(readOnly = true)
    public List<BidResponseDTO> findAllBids() {
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Delete bid safely
    @Override
    @Transactional
    public synchronized void deleteBid(Long id) {
        Bid bidToDelete = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + id));

        AuctionItem auctionItem = bidToDelete.getAuctionItem();

        // --- Check if auction already ended ---
        if (auctionItem.getEndTime().isBefore(Instant.now())) {
            throw new BidValidationException("Auction is already closed! Can't delete the bid.");
        }

        boolean isCurrentlyHighestBid =
                bidToDelete.getAmount().compareTo(auctionItem.getCurrentBid()) == 0 &&
                        bidToDelete.getBidder().getId().equals(auctionItem.getHighBidderUserId());

        repo.delete(bidToDelete);
        log.info("Bid deleted: Id={}, AuctionItem={}, User={}", id, auctionItem.getId(), bidToDelete.getBidder().getId());

        // --- Recalculate if deleted bid was highest ---
        if (isCurrentlyHighestBid) {
            Optional<Bid> nextHighest = repo.findTopByAuctionItemIdOrderByAmountDescBidTimeAsc(auctionItem.getId());
            String topic = "/topic/bids/" + auctionItem.getId();

            if (nextHighest.isPresent()) {
                Bid newHighest = nextHighest.get();
                auctionItem.setCurrentBid(newHighest.getAmount());
                auctionItem.setHighBidderUserId(newHighest.getBidder().getId());

                redisService.setAuction(auctionItem.getId(), newHighest.getAmount());
                messageTemplate.convertAndSend(topic, mapToResponse(newHighest));

                log.info("New highest bid after deletion: AuctionItem={}, NewHighest={}", auctionItem.getId(), newHighest.getAmount());

            } else {
                // Reset auction to starting price
                auctionItem.setCurrentBid(auctionItem.getStartPrice());
                auctionItem.setHighBidderUserId(null);

                redisService.deleteBid(auctionItem.getId());

                BidResponseDTO resetBidDTO = new BidResponseDTO();
                resetBidDTO.setAuctionItemId(auctionItem.getId());
                resetBidDTO.setAmount(auctionItem.getStartPrice());
                resetBidDTO.setBidderUserId(null);
                resetBidDTO.setSuccessful(false);

                messageTemplate.convertAndSend(topic, resetBidDTO);

                log.info("Auction reset to starting price: AuctionItem={}, StartPrice={}", auctionItem.getId(), auctionItem.getStartPrice());
            }
            auctionRepo.save(auctionItem);
        }
    }
}
