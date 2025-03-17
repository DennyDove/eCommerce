package com.denidove.trading.repositories;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    public List<CartItem> findAllByUserId(Long userId);

    public List<CartItem> findAllByUserIdAndStatus(Long userId, ProductStatus status);

    public List<CartItem> findAllByUserIdAndProductIdAndStatus(Long userId, Long productId, ProductStatus status);

    public void deleteByIdAndUserId(Long cartItemId, Long id);
}
