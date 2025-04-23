package org.example.delivery.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;

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

        @ManyToOne(fetch =FetchType.LAZY)
        @JoinColumn(name = "menu_id")
        private Menu menu;

        private int quantity;
        private int price;

        public static OrderItem of(CartItem cartItem){ //장바구니 목록가져오기
            OrderItem orderItem =new OrderItem();
            orderItem.menu=cartItem.getMenu();
            orderItem.quantity=cartItem.getQuantity();
            orderItem.price=cartItem.getPrice();
            return orderItem;
    }
}

