package org.example.delivery.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "carts_items")
public class CartItem { //장바구니에 담긴 메뉴
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;  //장바구니

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;  //메뉴

    private int quantity;   //수량

    private int price_snapshot; //메뉴 선택 시점의 가격
}