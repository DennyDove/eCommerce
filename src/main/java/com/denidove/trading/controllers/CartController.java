package com.denidove.trading.controllers;

import com.denidove.trading.dto.CartDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import com.denidove.trading.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class CartController {

    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;


    public CartController(UserService userService, ProductService productService,
                          CartItemService cartItemService) {
        this.userService = userService;
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @PostMapping("/buycoin")
    public ResponseEntity<?> buyCoin(@RequestParam(value = "id", required = true) Long id,
                                     @RequestParam(value = "quantity", required = true) Integer quantity) {

        cartItemService.save(new CartItem(), id, quantity); // product_id
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

