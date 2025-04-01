package com.denidove.trading.controllers;

import com.denidove.trading.TradingApplication;
import com.denidove.trading.dto.LoginDto;
import com.denidove.trading.services.LoginService;
import com.denidove.trading.services.UserSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        loginService.loginCheck(loginDto);
        boolean login = userSessionService.getAuthStatus();
        if(login) {
            log.info("User authentication successfull.");
        } else {
            log.info("Authentication failed.");
        }

    }
}
