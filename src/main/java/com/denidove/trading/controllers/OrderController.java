package com.denidove.trading.controllers;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.OrderService;
import com.denidove.trading.services.UserSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final UserSessionService userSessionService;

    public OrderController(CartItemService cartItemService,
                           OrderService orderService,
                           UserSessionService userSessionService) {
        this.orderService = orderService;
        this.userSessionService = userSessionService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model,
                            @RequestParam(value = "user", required = false) Long userId) {
        List<Order> orders = orderService.findAllByUserId(userSessionService.getUserId());
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();

        Boolean loginStatus = userSessionService.getLoginStatus();
        String userInit = "";
        if(loginStatus) userInit = userSessionService.getUserInit();

        model.addAttribute("userName", userSessionService.getUserName());
        model.addAttribute("orders", orders);
        model.addAttribute("coinsInCart", coinsInCart);
        model.addAttribute("userInit", userInit);
        return "user_orders.html";
    }

    @GetMapping("/orderitems")
    public String getOrders(Model model,
                            @RequestParam(value = "user", required = false) Long userId,
                            @RequestParam(value = "order", required = true) Long orderId) {
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();
        Order order = orderService.findByUserIdAndOrderId(userSessionService.getUserId(), orderId).get();
        List<CartItem> coins = order.getCartItem();

        Boolean loginStatus = userSessionService.getLoginStatus();
        String userInit = "";
        if(loginStatus) userInit = userSessionService.getUserInit();

        model.addAttribute("coinsInCart", coinsInCart);
        model.addAttribute("userInit", userInit);
        model.addAttribute("order", order);
        model.addAttribute("coins", coins);
        return "order_details.html";
    }

    @PostMapping("/order")
    public String makeOrder(Model model,
                            @RequestParam(value = "quantity", required = true) int[] quantity) {
        Long orderId = orderService.save(quantity); // сохраняем quantity[i] для каждого i-го товара, выбранного в корзине
        model.addAttribute("orderId", orderId);
        return "orderok.html";
    }
}


