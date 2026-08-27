package com.sneakerhub.sneakerhub.dto;

import lombok.Data;

@Data
public class CreateAuctionRequest {
    private String title;
    private String brand;
    private String description;
    private String size;
    private String condition;
    private String imageUrl;
    private Double startPrice;
    private Double buyNowPrice;
    private int durationHours; // how long the auction runs
}