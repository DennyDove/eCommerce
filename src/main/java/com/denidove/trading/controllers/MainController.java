package com.denidove.trading.controllers;

import com.denidove.trading.entities.Product;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.User;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import com.denidove.trading.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class MainController {

    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;


    public MainController(UserService userService, ProductService productService,
                          CartItemService cartItemService) {
        this.userService = userService;
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/index.html"))
                .build();
    }

    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestParam String name, @RequestParam BigDecimal price,
                                        @RequestParam MultipartFile picture,
                                        @RequestParam Integer quantity) throws IOException {
        Product product = new Product(name, price, picture.getBytes(), quantity);
        productService.save(product);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/index.html"))
                .build();
    }

    @PostMapping("/buycoin")
    public ResponseEntity<?> buyCoin(@RequestParam(value = "id", required = true) Long id,
                                        @RequestParam(value = "quantity", required = false) Integer quantity) {
        cartItemService.save(new CartItem(), id, quantity); // product_id
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/products"))
                .build();
    }

    @PostMapping("/deleteitem")
    //toDo
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

