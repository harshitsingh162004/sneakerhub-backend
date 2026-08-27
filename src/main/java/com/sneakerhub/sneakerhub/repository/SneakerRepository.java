package com.sneakerhub.sneakerhub.repository;

import com.sneakerhub.sneakerhub.entity.Sneaker;
import com.sneakerhub.sneakerhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SneakerRepository extends JpaRepository<Sneaker, Long> {
    List<Sneaker> findBySeller(User seller);
}