package org.example.delivery.domain.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long storeId;                      //가게
    private String storeName;
    private List<CartItemResponse> items;      //카트에 해당하는 item 리스트
    private int totalPrice;                    //priceSnapshot 기준 총 금액
}
