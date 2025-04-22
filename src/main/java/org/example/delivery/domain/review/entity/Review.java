package org.example.delivery.domain.review.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="reviews")
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToOne
    private Order order;

    @ManyToOne
    private Store store;

    private int rating;

    private String content;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
