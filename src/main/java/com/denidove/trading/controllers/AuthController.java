package com.denidove.trading.controllers;

import com.denidove.trading.dto.LoginDto;
import com.denidove.trading.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestScope
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public void auth(@RequestBody LoginDto loginDto) {

    }

    /*
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/products"))
                .build();
    }
    */

}
