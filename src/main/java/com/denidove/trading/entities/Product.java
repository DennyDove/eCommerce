package com.denidove.trading.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products") // важно указать имя создаваемой таблицы, чтобы оно не совпадало с именем класса
//@SequenceGenerator(name="my_seq", initialValue=1, allocationSize=100)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Integer cartQty;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    public Product(String name, BigDecimal price, byte[] picture, Integer quantity) {
        this.name = name;
        this.price = price;
        this.picture = picture;
        this.quantity = quantity;
    }

    public String getImageDataBase64() {
        return Base64.toBase64String(this.picture);
    }
}
