package org.example.delivery.domain.review.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name="reviews")
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToOne
    private Order order; //주문당 1개 리뷰

    private int rating;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;


    public static Review of(Order order, int rating, String content) {
        Review review = new Review();
        review.user=order.getUser();
        review.order=order;
        review.store=order.getStore();
        review.rating=rating;
        review.content=content;
        return review;
    }
}
