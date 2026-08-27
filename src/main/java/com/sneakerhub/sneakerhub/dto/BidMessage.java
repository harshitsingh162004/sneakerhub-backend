package com.sneakerhub.sneakerhub.dto;

import lombok.Data;

@Data
public class BidMessage {
    private Long auctionId;
    private Double bidAmount;
    private String bidderName;
    private Double newCurrentPrice;
    private int totalBids;
}
