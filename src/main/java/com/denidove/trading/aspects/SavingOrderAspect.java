package com.denidove.trading.aspects;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;

import com.denidove.trading.enums.OrderStatus;
import com.denidove.trading.enums.ProductStatus;
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
    public Long saving(ProceedingJoinPoint joinPoint) throws Throwable {
        //toDo - найденное решение: изменить "void" на "Long". Когда у метода акпекта стоит "void",
        // тогда результат работы аспекта всегда будет возвращать null (т.е "void")

        Object [] arguments = joinPoint.getArgs(); // получение аргументов перехватываемого метода

        int[] quantity = (int[]) arguments[1];
        Order order = new Order(new Timestamp(System.currentTimeMillis()));

        //toDo создать механизм верификации пользователя Security
        Long userId = userSessionService.getUserId();
        var user = userRepository.findById(userId).get();

        List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndStatus(userId, ProductStatus.InCart);
        int i = 0;
        for (CartItem item : productInCart) {
            item.setQuantity(quantity[i++]); // устанавливаем quantity[i] для каждого i-го товара, выбранного в корзине
            item.setStatus(ProductStatus.Ordered);
        }
        order.setUser(user);
        order.setCartItem(productInCart);
        order.setStatus(OrderStatus.InWork);

        Object[] newArguments = {order, quantity};
        joinPoint.proceed(newArguments); // запуск метода save(Order order, int[] quantity) с новыми аргументами
        return order.getId();
    }
}


