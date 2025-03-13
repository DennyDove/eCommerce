package com.denidove.trading.repositories;

import com.denidove.trading.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    public List<CartItem> findAllByUserIdAndProductId(Long userId, Long productId);
}
