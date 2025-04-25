package org.example.delivery.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.cart.entity.CartItem;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String menuName;
    private int quantity;
    private int price;

    public static OrderItem of(CartItem cartItem){ //장바구니 목록가져오기
        OrderItem orderItem =new OrderItem();
        orderItem.menuName=cartItem.getMenu().getName();
        orderItem.quantity=cartItem.getQuantity();
        orderItem.price= (int) cartItem.getPriceSnapshot();
        return orderItem;
    }
}

