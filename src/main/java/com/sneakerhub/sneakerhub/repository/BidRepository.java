package com.sneakerhub.sneakerhub.repository;

import com.sneakerhub.sneakerhub.entity.Bid;
import com.sneakerhub.sneakerhub.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionOrderByBidAmountDesc(Auction auction);
}