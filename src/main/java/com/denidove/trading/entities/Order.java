package com.denidove.trading.entities;

import com.denidove.trading.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders") // важно указать имя создаваемой таблицы, чтобы оно не совпадало с именем класса
//@SequenceGenerator(name="my_seq", initialValue=1, allocationSize=100)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "order_product",
                 joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
                 inverseJoinColumns = @JoinColumn(name="cart_item_id", referencedColumnName="id"))
    private List<CartItem> cartItem;

    @Column
    private Timestamp timeDate;

    @Column
    private OrderStatus status;

    public Order(Timestamp timeDate) {
        this.timeDate = timeDate;
    }
}
