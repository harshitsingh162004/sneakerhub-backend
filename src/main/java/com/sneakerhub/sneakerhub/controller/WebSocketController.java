package com.sneakerhub.sneakerhub.controller;

import com.sneakerhub.sneakerhub.dto.AuctionResponse;
import com.sneakerhub.sneakerhub.dto.BidMessage;
import com.sneakerhub.sneakerhub.dto.PlaceBidRequest;
import com.sneakerhub.sneakerhub.repository.UserRepository;
import com.sneakerhub.sneakerhub.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionService auctionService;
    private final UserRepository userRepository;

    @MessageMapping("/bid")
    public void placeBid(@Payload PlaceBidRequest request,
                         SimpMessageHeaderAccessor headerAccessor) {

        // get email from session attributes set during handshake
        String email = (String) headerAccessor.getSessionAttributes()
                .get("email");

        // fallback — use first user if session attr not set
        if (email == null) {
            email = userRepository.findAll()
                    .stream()
                    .filter(u -> !u.getRole().equals("SELLER"))
                    .findFirst()
                    .map(u -> u.getEmail())
                    .orElse(null);
        }

        if (email == null) return;

        AuctionResponse response = auctionService.placeBid(request, email);

        BidMessage message = new BidMessage();
        message.setAuctionId(response.getAuctionId());
        message.setBidAmount(request.getBidAmount());
        message.setBidderName(email);
        message.setNewCurrentPrice(response.getCurrentPrice());
        message.setTotalBids(response.getTotalBids());

        messagingTemplate.convertAndSend(
            "/topic/auction/" + request.getAuctionId(),
            message
        );
    }
}