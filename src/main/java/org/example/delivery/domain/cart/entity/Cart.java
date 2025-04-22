package org.example.delivery.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "carts")
public class Cart { //장바구니
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true) //유저는 1개의 장바구니만 사용 가능
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalDateTime expiredAt;             //장바구니 만료 시간, 수동 업데이트
}