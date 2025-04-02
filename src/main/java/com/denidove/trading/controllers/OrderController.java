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

    private final OrderService orderService;
    private final CartItemService cartItemService;
    private final UserSessionService userSessionService;

    public OrderController(OrderService orderService,
                           CartItemService cartItemService,
                           UserSessionService userSessionService) {
        this.orderService = orderService;
        this.cartItemService = cartItemService;
        this.userSessionService = userSessionService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model,
                            @RequestParam(value = "user", required = false) Long userId) {
        List<Order> orders = orderService.findAllByUserId(userSessionService.getUserId());
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();

        //toDo оптимизировать повторяющийся код (наверное вынести в отдельный класс)
        Boolean loginStatus = userSessionService.getAuthStatus();
        String userInit = "";
        if(loginStatus) userInit = userSessionService.getUserInit();

        model.addAttribute("userName", userSessionService.getUserName());
        model.addAttribute("orders", orders);
        model.addAttribute("coinsInCart", coinsInCart);
        model.addAttribute("userInit", userInit);
        return "user_orders.html";
    }

    @GetMapping("/orderitems")
    public String getOrderDetails(Model model,
                            @RequestParam(value = "user", required = false) Long userId,
                            @RequestParam(value = "order", required = true) Long orderId) {
        int coinsInCart = cartItemService.findAllByUserIdAndStatus().size();
        Order order = orderService.findByUserIdAndOrderId(userSessionService.getUserId(), orderId).get();
        List<CartItem> coins = order.getCartItem();

        //toDo оптимизировать повторяющийся код (наверное вынести в отдельный класс)
        Boolean loginStatus = userSessionService.getAuthStatus();
        String userInit = "";
        if(loginStatus) userInit = userSessionService.getUserInit();

        model.addAttribute("coinsInCart", coinsInCart);
        model.addAttribute("userInit", userInit);
        model.addAttribute("order", order);
        model.addAttribute("coins", coins);
        return "order_details.html";
    }

    @PostMapping("/order")
    public String saveOrder(Model model,
                            @RequestParam(value = "quantity", required = true) int[] quantity) {
        Boolean authStatus = userSessionService.getAuthStatus(); // Проверка авторизации
        // Сценарий для авторизовавшихся пользователей
        if(authStatus) {
                Long orderId = orderService.save(new Order(), quantity); // сохраняем quantity[i] для каждого i-го товара, выбранного в корзине
                model.addAttribute("orderId", orderId);
                return "orderok.html";
        }
        // Сценарий для неавторизовавшихся пользователей
        else {
            return "login.html";
        }
    }
}


