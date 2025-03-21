package com.denidove.trading.repositories;

import com.denidove.trading.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public void deleteByCartItemId(Long id);
}
