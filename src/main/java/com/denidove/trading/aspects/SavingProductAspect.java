package com.denidove.trading.aspects;

import com.denidove.trading.entities.Product;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.User;
import com.denidove.trading.exceptions.ItemQuantityException;
import com.denidove.trading.repositories.ProductRepository;
import com.denidove.trading.repositories.CartItemRepository;
import com.denidove.trading.repositories.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
// Аспект для метода save(ProductSelected productSelected, Long productId, Integer quantity)
// в классе CartItemService
public class SavingProductAspect {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public SavingProductAspect(UserRepository userRepository,
                                  ProductRepository productRepository,
                                  CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(SavingProductAspect.class.getName());

    @Around("execution(* com.denidove.trading.services.CartItemService.save(..))")
    public void saving(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName(); // получение имени метода (в информационных целях)
        Object [] arguments = joinPoint.getArgs(); // получение аргументов перехватываемого метода

        Long productId = (Long) arguments[1];
        Integer quantity = (Integer) arguments[2];
        CartItem cartItem = (CartItem) arguments[0]; // arguments[0];
        Product product = productRepository.findById(productId).orElseThrow();
        int prodQty = product.getQuantity();
        List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndProductId(1L, productId);
        // Суть данной конструкции if-else: если продукт cartItem не добален в корзину, то
        // добавить этот продукт через сеттеры и метод save();
        if(productInCart.isEmpty()) {
            User user = userRepository.findById(1L).get();
            if(quantity > prodQty) throw new ItemQuantityException(); // исключение превышения заказа над имеющимся товаром на складе
            cartItem.setProduct(product);
            cartItem.setUser(user); // добавили id пользователя
            cartItem.setQuantity(quantity);
        } else {
        //  Если данный продукт productSelected уже есть в корзине, то можем менять только количество или удаляем его
            cartItem = productInCart.getFirst();
            quantity+= cartItem.getQuantity();
            if(quantity > prodQty) throw new ItemQuantityException(); // исключение превышения заказа над имеющимся товаром на складе
            cartItem.setQuantity(quantity);
        }
        Object[] newArguments = {cartItem, productId, quantity};
        joinPoint.proceed(newArguments); // запуск метода save(CartItem cartItem, Long productId, Integer quantity)
                                         // с новыми аргументами
    }
}
