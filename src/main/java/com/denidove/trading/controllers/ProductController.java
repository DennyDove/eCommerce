package com.denidove.trading.controllers;

import com.denidove.trading.entities.Product;
import com.denidove.trading.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestParam String name, @RequestParam BigDecimal price,
                                        @RequestParam MultipartFile picture,
                                        @RequestParam Integer quantity) throws IOException {
        Product product = new Product(name, price, picture.getBytes(), quantity);
        productService.save(product);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/index.html"))
                .build();
    }

}
