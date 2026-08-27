package com.sneakerhub.sneakerhub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuctionResponse {
    private Long auctionId;
    private String sneakerTitle;
    private String brand;
    private String size;
    private String condition;
    private String imageUrl;
    private Double startPrice;
    private Double currentPrice;
    private Double buyNowPrice;
    private LocalDateTime endTime;
    private String status;
    private String sellerName;
    private int totalBids;
}