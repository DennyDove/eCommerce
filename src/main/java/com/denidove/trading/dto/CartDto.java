package com.denidove.trading.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor

public class CartDto {

    private Long productId;
    private Integer quantity;

    public CartDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

}
