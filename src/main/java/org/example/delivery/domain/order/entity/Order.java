package org.example.delivery.domain.order.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name="orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Menu menu;

    private String address;

    private int quantity;
    private int price;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;

    public enum Status{
        PENDING,
        COOKING,
        DELIVERING,
        DELIVERED,
        CANCELED
    }

    public static Order of(User user,Store store,Menu menu,int quantity,int priceSnapshot,String address){
        Order order = new Order();
        order.user = user;
        order.store = store;
        order.menu = menu;
        order.quantity = quantity;
        order.price = priceSnapshot;
        order.totalPrice = quantity * priceSnapshot;
        order.address = address;
        order.status = Status.PENDING;
        order.orderedAt = LocalDateTime.now();
        return order;
    }
}


