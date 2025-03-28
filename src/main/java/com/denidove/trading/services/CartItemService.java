package com.denidove.trading.services;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.enums.ProductStatus;
import com.denidove.trading.repositories.ProductRepository;
import com.denidove.trading.repositories.CartItemRepository;
import com.denidove.trading.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional   // Без @Transactional выдавала ошибку: Large Objects may not be used in auto-commit mode
public class CartItemService {

    private final UserSessionService userSessionService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(UserSessionService userSessionService,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           CartItemRepository cartItemRepository) {
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(CartItemService.class);

    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    // Метод выдает все добавленные в корзину товары (поиск в БД по: userId и статусу InCart
    public List<CartItem> findAllByUserIdAndStatus() {
        //toDo создать механизм верификации пользователя
        return cartItemRepository.findAllByUserIdAndStatus(userSessionService.getUserId(), ProductStatus.InCart);
    }

    public void save(CartItem cartItem, Long productId, Integer quantity) {
        // управление данного метода перехватывается соответствующим аспектом SavingProductAspect
        cartItemRepository.save(cartItem);
    }

    public void delete(Long cartItemId) {
        cartItemRepository.deleteByIdAndUserId(cartItemId, userSessionService.getUserId());
    }
}
