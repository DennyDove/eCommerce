package com.denidove.trading.aspects;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.entities.Product;
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

import java.util.HashMap;

@Component
@Aspect
// Аспект для метода save(ProductSelected productSelected, Long productId, Integer quantity)
// в классе CartItemService
public class SavingProductDtoAspect {

    private final UserSessionService userSessionService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public SavingProductDtoAspect(UserSessionService userSessionService,
                                  UserRepository userRepository,
                                  ProductRepository productRepository,
                                  CartItemRepository cartItemRepository) {
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(SavingProductDtoAspect.class.getName());

    @Around("execution(* com.denidove.trading.services.CartItemService.saveToDto(..))")
    public void saving(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName(); // получение имени метода (в информационных целях)
        Object [] arguments = joinPoint.getArgs(); // получение аргументов перехватываемого метода

        Long productId = (Long) arguments[1];
        Integer quantity = (Integer) arguments[2];
        CartItemDto cartItemDto = (CartItemDto) arguments[0];;

        Product product = productRepository.findById(productId).orElseThrow();
        int prodQty = product.getQuantity(); // определение количества продукта на складе

        //toDo Требуется существенно оптимизировать код!
        // Вместо List<CartItemDto> cartItemDtoList = new ArrayList<>();
        // попобовать сделать HashMap ? Но возникает проблемма с циклом for each?
        // DONE

        HashMap<Long, CartItemDto> productInCart = userSessionService.getCartItemDtoList();
        // Суть данной конструкции if-else: если продукта cartItemDto нет в списке, то
        // добавить этот продукт через сеттеры и метод add();
        if(productInCart.isEmpty()) {
            if(quantity > prodQty) throw new ItemQuantityException(); // исключение превышения заказа над имеющимся товаром на складе
            cartItemDto.setProduct(product);
            cartItemDto.setQuantity(quantity);
            cartItemDto.setStatus(ProductStatus.InCart);

        } else {
        //  Если данный продукт CartItemDto уже есть в корзине, то можем менять только количество или удаляем его
            cartItemDto = productInCart.get(productId);
            if(cartItemDto == null) {
                if(quantity > prodQty) throw new ItemQuantityException(); // исключение превышения заказа над имеющимся товаром на складе
                cartItemDto = new CartItemDto();
                cartItemDto.setProduct(product);
                cartItemDto.setQuantity(quantity);
                cartItemDto.setStatus(ProductStatus.InCart);
            } else {
                quantity += cartItemDto.getQuantity();
                if (quantity > prodQty)
                    throw new ItemQuantityException(); // исключение превышения заказа над имеющимся товаром на складе
                cartItemDto.setQuantity(quantity);
            }
        }
        Object[] newArguments = {cartItemDto, productId, quantity};
        joinPoint.proceed(newArguments); // запуск метода saveToDto(CartItemDto cartItemDto, Long productId, Integer quantity)
                                         // с новыми аргументами
    }
}
