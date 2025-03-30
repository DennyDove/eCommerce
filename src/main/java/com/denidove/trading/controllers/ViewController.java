package com.denidove.trading.controllers;

import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import com.denidove.trading.services.UserSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ProductService productService;
    private final CartItemService cartItemService;
    private final UserSessionService userSessionService;

    public ViewController(ProductService productService,
                          CartItemService cartItemService,
                          UserSessionService userSessionService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.userSessionService = userSessionService;
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        var products = productService.findAll();
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();

        Boolean loginStatus = userSessionService.getLoginStatus();
        String userInit = "";
        if(loginStatus) userInit = userSessionService.getUserInit();

        model.addAttribute("products", products);
        model.addAttribute("coinsInCart", coinsInCart);
        model.addAttribute("userInit", userInit);
        //model.addAttribute("coinsNumber", coinsNumber);
        if(loginStatus)
            return "products_user.html";
        else return "products.html";

    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        var coins = cartItemService.findAllByUserIdAndStatus();
        model.addAttribute("coins", coins);
        return "cart.html";
    }



}

