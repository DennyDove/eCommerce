package com.denidove.trading.controllers;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.UserSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class CartController {

    private final CartItemService cartItemService;
    private final UserSessionService userSessionService;


    public CartController(CartItemService cartItemService, UserSessionService userSessionService) {
        this.cartItemService = cartItemService;
        this.userSessionService = userSessionService;
    }

    // Добавление товара в корзину
    @PostMapping("/buycoin")
    public ResponseEntity<?> buyCoin(@RequestParam(value = "id", required = true) Long id,
                                     @RequestParam(value = "quantity", required = true) Integer quantity) {

        Boolean authStatus = userSessionService.getAuthStatus();

        // проверка - если пользователь авторизован, тогда товары добавляем сразу в БД (таблица CartItems)
        if(authStatus) {
            cartItemService.save(new CartItem(), id, quantity); // product_id
        } else {
            // если пользователь неавторизован, тогда товары добавляем во временную коллецию List<CartItemDto> в классе UserSessionService
            cartItemService.saveToDto(new CartItemDto(), id, quantity);
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/products"))
                .build();
    }

    @DeleteMapping("/deleteitem")
    public void deleteCartItem(@RequestParam(value = "id", required = true) Long cartItemId) {
        cartItemService.delete(cartItemId);
    }
}
        /* Используем аспектный подход ExceptionControllerAdvice
           Вместо конструкции try-catch:
        try {
            cartItemService.save(productSelected, id, quantity);
        } catch (ItemQuantityException e) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:8080/products"))
                    .build();
        }
        */

