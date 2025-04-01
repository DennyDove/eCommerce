package com.denidove.trading.controllers;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import com.denidove.trading.services.UserSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

        //toDo оптимизировать повторяющийся код
        Boolean loginStatus = userSessionService.getAuthStatus();
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
        Boolean loginStatus = userSessionService.getAuthStatus();
        if(loginStatus) {
            var unauthorizedUserCart = userSessionService.getCartItemDtoList();
            if(unauthorizedUserCart.isEmpty()) {
                var coins = cartItemService.findAllByUserIdAndStatus();
                model.addAttribute("coins", coins);
            }

            // Условие, если пользователь авторизовался, но уже добавлял до этого товар в корзину в рамках сессии в неавторизованном виде
            if(!unauthorizedUserCart.isEmpty()) {
                List<CartItemDto> coins = userSessionService.getCartItemDtoList();
                model.addAttribute("coins", coins);
            }

        } else {
            List<CartItemDto> coins = userSessionService.getCartItemDtoList();
            model.addAttribute("coins", coins);
        }
        return "cart.html";
    }



}

