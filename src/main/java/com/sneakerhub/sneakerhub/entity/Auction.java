package com.sneakerhub.sneakerhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auctions")
@Data
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sneaker_id")
    private Sneaker sneaker;

    private Double startPrice;
    private Double currentPrice;
    private Double buyNowPrice;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status = "ACTIVE"; // ACTIVE, ENDED, CANCELLED

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
}