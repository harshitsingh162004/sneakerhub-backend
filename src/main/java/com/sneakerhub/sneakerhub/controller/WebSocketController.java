package com.sneakerhub.sneakerhub.controller;

import com.sneakerhub.sneakerhub.dto.AuctionResponse;
import com.sneakerhub.sneakerhub.dto.BidMessage;
import com.sneakerhub.sneakerhub.dto.PlaceBidRequest;
import com.sneakerhub.sneakerhub.security.JwtUtil;
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
    private final JwtUtil jwtUtil;

    @MessageMapping("/bid")
    public void placeBid(@Payload PlaceBidRequest request,
                         SimpMessageHeaderAccessor headerAccessor) {

        // extract JWT token from STOMP headers
        String authHeader = (String) headerAccessor
                .getSessionAttributes()
                .getOrDefault("token", null);

        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.isValid(token)) {
                email = jwtUtil.extractEmail(token);
            }
        }

        if (email == null) {
            System.out.println("WebSocket bid rejected — no valid token");
            return;
        }

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