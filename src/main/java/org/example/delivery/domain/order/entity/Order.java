package org.example.delivery.domain.order.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItem;

    private String address;

    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;

    public enum Status {
        PENDING("주문이 접수되었습니다."),
        COOKING("주문이 준비 중입니다."),
        DELIVERING("주문이 배송 중입니다."),
        DELIVERED("주문이 배송되었습니다."),
        CANCELED("주문이 취소되었습니다.");

        private final String message;

        Status(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public void cancel() {
        this.status = Status.CANCELED;
    }

    public void updateStatus(String status) {
        if (this.status.equals(Status.CANCELED) || this.status.equals(Status.DELIVERED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 주문은 변경할 수 없습니다.");
        }
        this.status = Status.valueOf(status.toUpperCase()); //입력받은 문자열을 enum으로 변경

        if (this.status.equals(Status.DELIVERED)) { //배달완료시 배달시간 기록
            this.deliveredAt = LocalDateTime.now();
        }
    }


    public static Order of(Cart cart, Store store, List<OrderItem> orderItem, String address) {
        Order order = new Order();
        order.user = cart.getUser();
        order.store = store;
        order.orderItem = orderItem;
        order.address = address;
        order.status = Status.PENDING;
        order.orderedAt = LocalDateTime.now();
        order.totalPrice=orderItem.stream().
                mapToInt(item->item.getPrice()*item.getQuantity())
                .sum();
        return order;
    }
}


