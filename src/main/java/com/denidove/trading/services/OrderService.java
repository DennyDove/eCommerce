package com.denidove.trading.services;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;
import com.denidove.trading.enums.ProductStatus;
import com.denidove.trading.repositories.CartItemRepository;
import com.denidove.trading.repositories.OrderRepository;
import com.denidove.trading.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderService {

    private final UserSessionService userSessionService;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserSessionService userSessionService,
                        UserRepository userRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.userSessionService = userSessionService;
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
                            // найти заказ по id заказа и id пользователя для последующего просмотра элементов заказа (CartItem)
    }

    // Метод для записи заказа в БД из корзины авторизованного пользователя
    // Управление данного метода перехватывается соответствующим аспектом SavingOrderAspect
    @Transactional
    public Long save(Order order, int[] quantity) {
        return orderRepository.save(order).getId();
    }

    // Просто сохраняем состояние корзины неавторизованного пользователя (по quantity)
    public void saveToDto(int[] quantity) {
        HashMap<Long, CartItemDto> productInCart = userSessionService.getCartItemDtoList();
        int i = 0;
        // Ниже приведен синтаксическая структура для перебора HashMap в цикле for-each
        for(Map.Entry<Long, CartItemDto> item : productInCart.entrySet()) {
            item.getValue().setQuantity(quantity[i++]); // устанавливаем quantity для каждого i-го товара, выбранного в корзине
        }
    }
}



