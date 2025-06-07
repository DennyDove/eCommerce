package com.denidove.trading.services;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.dto.LoginDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.User;
import com.denidove.trading.enums.ProductStatus;
import com.denidove.trading.repositories.CartItemRepository;
import com.denidove.trading.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Сервисный класс, осуществляющий базовый функционал по верификации введенных учетных данных пользователя
@Service
@RequestScope
public class LoginService {

    private final UserRepository userRepository;
    private final UserSessionService userSessionService;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;

    public LoginService(UserRepository userRepository, UserSessionService userSessionService,
                        CartItemService cartItemService, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.userSessionService = userSessionService;
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;

    }

    public User loginCheck (LoginDto loginDto) {
        User user = userRepository.findUserByLogin(loginDto.getUsername()).get();
        if(loginDto.getPassword().equals(user.getPassword())) {
            userSessionService.setAuthStatus(true);
            userSessionService.setUserId(user.getId());
            userSessionService.setUserName(user.getName());
            userSessionService.setUserInit(String.valueOf((user.getName().charAt(0)))); // получаем и сохраняем первую букву имени
        }
        return user;
    }

    @Transactional
    public void persistProductInCart (HashMap<Long, CartItemDto> productInCartDto, User user) {
        List<CartItem> productInCart = cartItemRepository.findAllByUserIdAndStatus(user.getId(), ProductStatus.InCart);
        for (Map.Entry<Long, CartItemDto> itemDto : productInCartDto.entrySet()) {
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(itemDto.getValue().getProduct());
            item.setQuantity(itemDto.getValue().getQuantity());
            item.setStatus(ProductStatus.InCart);
            productInCart.add(item);
        }
        userSessionService.getCartItemDtoList().clear();
        cartItemRepository.saveAll(productInCart);
    }

}