package com.sneakerhub.sneakerhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sneakers")
@Data
public class Sneaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String brand;
    private String description;
    private String size;

    @Column(name = "sneaker_condition")
    private String condition;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
}