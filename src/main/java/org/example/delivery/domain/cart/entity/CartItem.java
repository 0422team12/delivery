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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;  //장바구니

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;  //메뉴

    private int quantity;   //수량

    @Column(name = "price_snapshot")
    private long priceSnapshot; //메뉴 선택 시점의 가격

    private CartItem(Cart cart, Menu menu, int quantity){
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
        this.priceSnapshot = menu.getPrice();
    }

    public static CartItem createCartItem(Cart cart, Menu menu, int quantity){
        return new CartItem(cart, menu, quantity);
    }

    public void updateQuantity(int quantity){
        this.quantity = quantity;
    }
}