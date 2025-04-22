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

    @OneToOne
    private Menu menu;

    private String address;

//    private Enum status;

    private LocalDateTime ordered_at;

    private LocalDateTime delivered_at;

}
