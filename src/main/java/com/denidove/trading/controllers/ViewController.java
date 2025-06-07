package com.denidove.trading.controllers;

import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.User;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.ProductService;
import com.denidove.trading.services.UserService;
import com.denidove.trading.services.UserSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller
public class ViewController {

    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final UserSessionService userSessionService;

    public ViewController(UserService userService, ProductService productService,
                          CartItemService cartItemService,
                          UserSessionService userSessionService) {
        this.userService = userService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.userSessionService = userSessionService;
    }

    @GetMapping("/userprofile")
    public String userProfile(Model model) {
        Boolean loginStatus = userSessionService.getAuthStatus();
        User user = userService.findById(userSessionService.getUserId()).get();
        if(loginStatus) {
            var userInit = userSessionService.getUserInit();
            int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();
            model.addAttribute("userInit", userInit);
            model.addAttribute("coinsInCart", coinsInCart);

            model.addAttribute("name", user.getName());
        }
        return "user_profile.html";
    }

    @GetMapping("/userdetails")
    public String getUser(Model model) {
        Boolean loginStatus = userSessionService.getAuthStatus();
        User user = userService.findById(userSessionService.getUserId()).get();
        if(loginStatus) {
            var userInit = userSessionService.getUserInit();
            int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();
            model.addAttribute("userInit", userInit);
            model.addAttribute("coinsInCart", coinsInCart);

            model.addAttribute("name", user.getName());
            model.addAttribute("login", user.getLogin());
            model.addAttribute("age", user.getAge());

        }
        return "user_details.html";
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        var products = productService.findAll();
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();
        // Для отбражения значка наполненной корзины неавторизованного пользователя
        if(!userSessionService.getCartItemDtoList().isEmpty() && coinsInCart == 0) {
            coinsInCart = userSessionService.getCartItemDtoList().size();
        }

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
        // Отображение корзины авторизовавшегося пользователя
        if(loginStatus) {
            var userInit = userSessionService.getUserInit();
            var coins = cartItemService.findAllByUserIdAndStatus();
            model.addAttribute("userInit", userInit);
            model.addAttribute("coins", coins);
            return "cart_auth.html";

            /* Закомментил, т.к. сделал, что при авторизации корзина сразу записывается в БД
            var unauthorizedUserCart = userSessionService.getCartItemDtoList();
            if(unauthorizedUserCart.isEmpty()) {
                var coins = cartItemService.findAllByUserIdAndStatus();
                model.addAttribute("coins", coins);
            }

            // Условие, если пользователь авторизовался, но уже добавлял до этого товар в корзину в рамках сессии в неавторизованном виде
            if(!unauthorizedUserCart.isEmpty()) {
                // Трансформируем HashMap в коллекцию List<CartItemDto>
                List<CartItemDto> coins = new ArrayList<>(userSessionService.getCartItemDtoList().values());
                model.addAttribute("coins", coins);
            }
            */

        } else {
            // Отображение корзины неавторизовавшегося пользователя
            // Трансформируем HashMap в коллекцию List<CartItemDto>
            List<CartItemDto> coins = new ArrayList<>(userSessionService.getCartItemDtoList().values());
            model.addAttribute("coins", coins);
        }
        return "cart.html";
    }

    @GetMapping("/login")
    public String login() {
            return "login.html";

    }
}

