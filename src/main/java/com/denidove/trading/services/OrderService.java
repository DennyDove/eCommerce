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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public Long save(int[] quantity) {
        Order order = new Order(new Timestamp(System.currentTimeMillis()));
        //toDo создать механизм верификации пользователя
        Long userId = userSessionService.getUserId();
        var user = userRepository.findById(userId).get();
        List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndStatus(userId, ProductStatus.InCart);
        int i = 0;
        for(CartItem item : productInCart) {
            item.setQuantity(quantity[i++]); // устанавливаем quantity[i] для каждого i-го товара, выбранного в корзине
            item.setStatus(ProductStatus.Ordered);
        }
        order.setUser(user);
        order.setCartItem(productInCart);
        return orderRepository.save(order).getId();
    }

    // Метод для записи заказа в БД из корзины неавторизованного пользователя, который потом авторизовался
    @Transactional
    public Long saveUnautorized(int[] quantity) {
        Order order = new Order(new Timestamp(System.currentTimeMillis()));
        //toDo создать механизм верификации пользователя
        Long userId = userSessionService.getUserId();
        var user = userRepository.findById(userId).get();

        // Создаем новый список CartItem чтобы в него потом занести элементы из productInCart (в цикле foreach)
        List<CartItem> productForPersist = new ArrayList<>();
        List<CartItemDto> productInCart = userSessionService.getCartItemDtoList();
        int i = 0;
        for(CartItemDto item : productInCart) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(quantity[i++]); // устанавливаем quantity[i] для каждого i-го товара, выбранного в корзине
            cartItem.setUser(user);
            cartItem.setProduct(item.getProduct());
            cartItem.setStatus(ProductStatus.Ordered);
            productForPersist.add(cartItem); // добавляем элемент в коллекцию, которую потом будем заносить в БД
        }
        order.setUser(user);
        order.setCartItem(productForPersist);
        userSessionService.getCartItemDtoList().clear(); // очищаем корзину
        return orderRepository.save(order).getId();
    }

}



