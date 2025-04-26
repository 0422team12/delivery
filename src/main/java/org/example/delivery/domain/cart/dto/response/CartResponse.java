package org.example.delivery.domain.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.store.entity.Store;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long cartId;
    private Long storeId;                      //가게
    private String storeName;
    private List<CartItemResponse> items;      //카트에 해당하는 item 리스트
    private long totalPrice;                    //priceSnapshot 기준 총 금액
    private boolean isOrderAvailable;           //최소 주문 금액 충족 여부
    private String message;

    public static CartResponse createCartResponse(Long cartId, Store store, List<CartItemResponse> items, Long totalPrice, boolean isOrderAvailable) {
        String message = isOrderAvailable
                ? "주문 가능합니다"
                : "최소 주문 금액은 " + store.getMinOrderValue() + "원입니다";
        return new CartResponse(cartId, store.getId(), store.getName(), items, totalPrice, isOrderAvailable, message);
    }
}
