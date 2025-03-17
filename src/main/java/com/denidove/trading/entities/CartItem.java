package com.denidove.trading.entities;

import com.denidove.trading.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart_item") // важно указать имя создаваемой таблицы, чтобы оно не совпадало с именем класса
//@SequenceGenerator(name="my_seq", initialValue=1, allocationSize=100)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Column
    private Integer quantity;

    @Column
    private ProductStatus status;

    public CartItem(User user, Product product, Integer quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }
}

