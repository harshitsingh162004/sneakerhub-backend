package com.sneakerhub.sneakerhub.controller;

import com.sneakerhub.sneakerhub.dto.*;
import com.sneakerhub.sneakerhub.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    // seller creates auction — needs JWT
    @PostMapping("/create")
    public ResponseEntity<AuctionResponse> create(
            @RequestBody CreateAuctionRequest request,
            Principal principal) {
        return ResponseEntity.ok(
            auctionService.createAuction(request, principal.getName()));
    }

    // anyone can browse — needs JWT
    @GetMapping
    public ResponseEntity<List<AuctionResponse>> getAll() {
        return ResponseEntity.ok(auctionService.getAllActiveAuctions());
    }

    // get single auction by id
    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    // buyer places a bid — needs JWT
    @PostMapping("/bid")
    public ResponseEntity<AuctionResponse> placeBid(
            @RequestBody PlaceBidRequest request,
            Principal principal) {
        return ResponseEntity.ok(
            auctionService.placeBid(request, principal.getName()));
    }
}