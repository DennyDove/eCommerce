package com.denidove.trading.services;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;
import com.denidove.trading.enums.ProductStatus;
import com.denidove.trading.repositories.CartItemRepository;
import com.denidove.trading.repositories.OrderRepository;
import com.denidove.trading.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void save() {
        Order order = new Order(new Timestamp(System.currentTimeMillis()));
        //toDo создать механизм верификации пользователя
        var user = userRepository.findById(1L).get();
        //List<CartItem> productInCart = cartItemRepository.findAllByUserId(1L);
        List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndStatus(1L, ProductStatus.InCart);
        for(CartItem item : productInCart) {
            item.setStatus(ProductStatus.Ordered);
        }
        order.setUser(user);
        order.setCartItem(productInCart);
        orderRepository.save(order);
    }

}



