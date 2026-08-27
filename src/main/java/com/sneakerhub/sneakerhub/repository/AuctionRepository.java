package com.sneakerhub.sneakerhub.repository;

import com.sneakerhub.sneakerhub.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatus(String status);
}