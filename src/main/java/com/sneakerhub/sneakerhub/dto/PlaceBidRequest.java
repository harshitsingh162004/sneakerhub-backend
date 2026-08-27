package com.sneakerhub.sneakerhub.dto;

import lombok.Data;

@Data
public class PlaceBidRequest {
    private Long auctionId;
    private Double bidAmount;
}