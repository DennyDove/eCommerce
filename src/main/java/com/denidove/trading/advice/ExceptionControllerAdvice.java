package com.denidove.trading.advice;


import com.denidove.trading.exceptions.ItemQuantityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ItemQuantityException.class)
    public ResponseEntity<?> exceptionItemQuantityhandler() {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/products"))
                .build();
    }
}
