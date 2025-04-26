package com.denidove.trading.repositories;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;
import com.denidove.trading.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findAllByStatus(OrderStatus status);

    public List<Order> findAllByUserId(Long id);

    // Если хотим найти по своему id, то никогда не нужно ставить имя текущей сущности (orderId), т.е. нужно просто писать id, а не orderId. Иначе, выдается ошибка ниже:
    public Optional<Order> findByIdAndUserId(Long orderId, Long userId); // orderId --- No property 'orderId' found for type 'Order'

    public void deleteByCartItemId(Long id);
}

