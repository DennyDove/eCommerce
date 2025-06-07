package com.denidove.trading.controllers;

import com.denidove.trading.TradingApplication;
import com.denidove.trading.dto.CartItemDto;
import com.denidove.trading.dto.LoginDto;
import com.denidove.trading.entities.User;
import com.denidove.trading.services.LoginService;
import com.denidove.trading.services.UserSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;

@RestController
public class AuthController {

    private final LoginService loginService;
    private final UserSessionService userSessionService;

    public AuthController(LoginService loginService, UserSessionService userSessionService) {
        this.loginService = loginService;
        this.userSessionService = userSessionService;
    }

    private static final Logger log = LoggerFactory.getLogger(TradingApplication.class);

    @PostMapping("/login")
    public void auth(@RequestBody LoginDto loginDto) {
        // Проверка логина, пароля и одновременно запись пользователя в переменную user;
        User user = loginService.loginCheck(loginDto);
        boolean login = userSessionService.getAuthStatus();
        if(login) {
            HashMap<Long, CartItemDto> productInCartDto = userSessionService.getCartItemDtoList();
            if(!productInCartDto.isEmpty()) {
                loginService.persistProductInCart(productInCartDto, user);
            }

            log.info("User authentication successfull.");
        } else {
            log.info("Authentication failed.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        userSessionService.setAuthStatus(false);
        userSessionService.setUserName(null);
        userSessionService.setUserId(null);
        userSessionService.setUserInit(null);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/products"))
                .build();
    }
}