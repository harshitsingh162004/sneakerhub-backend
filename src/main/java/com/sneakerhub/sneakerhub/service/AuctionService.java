package com.sneakerhub.sneakerhub.service;

import com.sneakerhub.sneakerhub.dto.*;
import com.sneakerhub.sneakerhub.entity.*;
import com.sneakerhub.sneakerhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final SneakerRepository sneakerRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public AuctionResponse createAuction(CreateAuctionRequest request, String email) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // create and save sneaker
        Sneaker sneaker = new Sneaker();
        sneaker.setTitle(request.getTitle());
        sneaker.setBrand(request.getBrand());
        sneaker.setDescription(request.getDescription());
        sneaker.setSize(request.getSize());
        sneaker.setCondition(request.getCondition());
        sneaker.setImageUrl(request.getImageUrl());
        sneaker.setSeller(seller);
        sneakerRepository.save(sneaker);

        // create and save auction
        Auction auction = new Auction();
        auction.setSneaker(sneaker);
        auction.setSeller(seller);
        auction.setStartPrice(request.getStartPrice());
        auction.setCurrentPrice(request.getStartPrice());
        auction.setBuyNowPrice(request.getBuyNowPrice());
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(LocalDateTime.now().plusHours(request.getDurationHours()));
        auction.setStatus("ACTIVE");
        auctionRepository.save(auction);

        return mapToResponse(auction, 0);
    }

    public List<AuctionResponse> getAllActiveAuctions() {
        return auctionRepository.findByStatus("ACTIVE")
                .stream()
                .map(auction -> {
                    int totalBids = bidRepository
                            .findByAuctionOrderByBidAmountDesc(auction).size();
                    return mapToResponse(auction, totalBids);
                })
                .collect(Collectors.toList());
    }

    public AuctionResponse getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        int totalBids = bidRepository
                .findByAuctionOrderByBidAmountDesc(auction).size();
        return mapToResponse(auction, totalBids);
    }

    public AuctionResponse placeBid(PlaceBidRequest request, String email) {
        User bidder = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // validations
        if (!auction.getStatus().equals("ACTIVE"))
            throw new RuntimeException("Auction is not active");

        if (LocalDateTime.now().isAfter(auction.getEndTime()))
            throw new RuntimeException("Auction has ended");

        if (request.getBidAmount() <= auction.getCurrentPrice())
            throw new RuntimeException("Bid must be higher than current price: "
                    + auction.getCurrentPrice());

        if (auction.getSeller().getEmail().equals(email))
            throw new RuntimeException("Seller cannot bid on their own auction");

        // save bid
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(bidder);
        bid.setBidAmount(request.getBidAmount());
        bidRepository.save(bid);

        // update auction current price
        auction.setCurrentPrice(request.getBidAmount());
        auctionRepository.save(auction);

        int totalBids = bidRepository
                .findByAuctionOrderByBidAmountDesc(auction).size();
        return mapToResponse(auction, totalBids);
    }

    private AuctionResponse mapToResponse(Auction auction, int totalBids) {
        AuctionResponse res = new AuctionResponse();
        res.setAuctionId(auction.getId());
        res.setSneakerTitle(auction.getSneaker().getTitle());
        res.setBrand(auction.getSneaker().getBrand());
        res.setSize(auction.getSneaker().getSize());
        res.setCondition(auction.getSneaker().getCondition());
        res.setImageUrl(auction.getSneaker().getImageUrl());
        res.setStartPrice(auction.getStartPrice());
        res.setCurrentPrice(auction.getCurrentPrice());
        res.setBuyNowPrice(auction.getBuyNowPrice());
        res.setEndTime(auction.getEndTime());
        res.setStatus(auction.getStatus());
        res.setSellerName(auction.getSeller().getName());
        res.setTotalBids(totalBids);
        return res;
    }
}