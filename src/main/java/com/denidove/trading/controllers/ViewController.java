package com.denidove.trading.controllers;

import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ProductService productService;
    private final CartItemService cartItemService;

    public ViewController(ProductService productService,
                          CartItemService cartItemService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        var products = productService.findAll();
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();
        model.addAttribute("products", products);
        model.addAttribute("coins", coinsInCart);
        //model.addAttribute("coinsNumber", coinsNumber);
        return "products.html";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        var coins = cartItemService.findAllByUserIdAndStatus();
        model.addAttribute("coins", coins);
        return "cart.html";
    }



}

