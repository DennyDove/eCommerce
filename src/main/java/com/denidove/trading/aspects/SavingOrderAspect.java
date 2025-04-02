package com.denidove.trading.aspects;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;
import com.denidove.trading.entities.Product;
import com.denidove.trading.entities.User;
import com.denidove.trading.enums.ProductStatus;
import com.denidove.trading.exceptions.ItemQuantityException;
import com.denidove.trading.repositories.CartItemRepository;
import com.denidove.trading.repositories.ProductRepository;
import com.denidove.trading.repositories.UserRepository;
import com.denidove.trading.services.UserSessionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
// Аспект для метода save(ProductSelected productSelected, Long productId, Integer quantity)
// в классе CartItemService
public class SavingOrderAspect {

    private final UserSessionService userSessionService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public SavingOrderAspect(UserSessionService userSessionService,
                             UserRepository userRepository,
                             ProductRepository productRepository,
                             CartItemRepository cartItemRepository) {
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(SavingOrderAspect.class.getName());

    @Around("execution(* com.denidove.trading.services.OrderService.save(..))")
    public void saving(ProceedingJoinPoint joinPoint) throws Throwable {
        Object [] arguments = joinPoint.getArgs(); // получение аргументов перехватываемого метода

        int[] quantity = (int[]) arguments[1];
        Order order = new Order(new Timestamp(System.currentTimeMillis()));

        //toDo создать механизм верификации пользователя
        Long userId = userSessionService.getUserId();
        var user = userRepository.findById(userId).get();
        var unauthorizedUserCart = userSessionService.getCartItemDtoList();

        // Если неавторизованный пользователь не добавлял ранее товар в корзину, тогда выполняется следующий блок:
        if(unauthorizedUserCart.isEmpty()) {
            List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndStatus(userId, ProductStatus.InCart);
            int i = 0;
            for (CartItem item : productInCart) {
                item.setQuantity(quantity[i++]); // устанавливаем quantity[i] для каждого i-го товара, выбранного в корзине
                item.setStatus(ProductStatus.Ordered);
            }
            order.setUser(user);
            order.setCartItem(productInCart);

            Object[] newArguments = {order, quantity};
            joinPoint.proceed(newArguments); // запуск метода save(Order order, int[] quantity) с новыми аргументами

        } else {
            // Если неавторизованный пользователь уже добавлял ранее товар в корзину:
            // Создаем новый список CartItem чтобы в него потом занести элементы из productInCart (в цикле foreach)
            List<CartItem> productToPersist = new ArrayList<>();
            List<CartItemDto> productInCart = userSessionService.getCartItemDtoList();
            int i = 0;
            for(CartItemDto item : productInCart) {
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(item.getQuantity()); // устанавливаем quantity для каждого i-го товара, выбранного в корзине
                cartItem.setUser(user);
                cartItem.setProduct(item.getProduct());
                cartItem.setStatus(ProductStatus.Ordered);
                productToPersist.add(cartItem); // добавляем элемент в коллекцию, которую потом будем заносить в БД
            }
            order.setUser(user);
            order.setCartItem(productToPersist);
            userSessionService.getCartItemDtoList().clear(); // очищаем корзину

            Object[] newArguments = {order, quantity};
            joinPoint.proceed(newArguments); // запуск метода save(Order order, int[] quantity) с новыми аргументами
        }
    }
}

