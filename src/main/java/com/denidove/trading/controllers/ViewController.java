package com.denidove.trading.controllers;

import com.denidove.trading.enums.ProductStatus;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import com.denidove.trading.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;

    public ViewController(UserService userService, ProductService productService,
                          CartItemService cartItemService) {
        this.userService = userService;
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        var products = productService.findAll();
        var coins = cartItemService.findAll();
        int coinsNumber = coins.size();
        model.addAttribute("products", products);
        model.addAttribute("coinsNumber", coinsNumber);
        return "products.html";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        //var coins = cartItemService.findAll();
        var coins = cartItemService.findAllByUserIdAndStatus();
        model.addAttribute("coins", coins);
        return "cart.html";
    }

}

