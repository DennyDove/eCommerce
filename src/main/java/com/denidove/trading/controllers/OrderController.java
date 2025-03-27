package com.denidove.trading.controllers;

import com.denidove.trading.entities.CartItem;
import com.denidove.trading.entities.Order;
import com.denidove.trading.services.CartItemService;
import com.denidove.trading.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model,
                            @RequestParam(value = "user", required = false) Long userId) {
        List<Order> orders = orderService.findAllByUserId(1L);
        model.addAttribute("orders", orders);
        return "user_orders.html";
    }

    @GetMapping("/orderitems")
    public String getOrders(Model model,
                            @RequestParam(value = "user", required = false) Long userId,
                            @RequestParam(value = "order", required = true) Long orderId) {
        Order order = orderService.findByUserIdAndOrderId(1L, orderId).get();
        List<CartItem> coins = order.getCartItem();

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


