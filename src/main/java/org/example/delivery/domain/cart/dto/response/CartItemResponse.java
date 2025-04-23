package org.example.delivery.domain.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long cartItemId;
    private Long menuId;
    private String menuName;
    private int quantity;       //수량
    private int priceSnapshot;  //장바구니 담겼을 시점의 가격
}
