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
import java.util.Optional;

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

    public List<Order> findAll() {
        return orderRepository.findAll();

    }

    @Transactional
    public List<Order> findAllByUserId(Long id) {
        return orderRepository.findAllByUserId(id);

    }

    @Transactional
    public Optional<Order> findByUserIdAndOrderId(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId);
                            // найти заказ по id заказа и id пользователя
    }


    @Transactional
    public Long save(int[] quantity) {
        Order order = new Order(new Timestamp(System.currentTimeMillis()));
        //toDo создать механизм верификации пользователя
        var user = userRepository.findById(1L).get();
        List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndStatus(1L, ProductStatus.InCart);
        int i = 0;
        for(CartItem item : productInCart) {
            item.setQuantity(quantity[i++]); // устанавливаем quantity[i] для каждого i-го товара, выбранного в корзине
            item.setStatus(ProductStatus.Ordered);
        }
        order.setUser(user);
        order.setCartItem(productInCart);
        return orderRepository.save(order).getId();
    }

}



