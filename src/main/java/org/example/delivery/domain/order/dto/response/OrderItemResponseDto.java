package org.example.delivery.domain.order.dto.response;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.order.entity.OrderItem;

@RequiredArgsConstructor
public class OrderItemResponseDto {

    private final String menuName;
    private final int quantity;
    private final int price;
    private final int totalPrice;

    public static OrderItemResponseDto toDto(OrderItem item){
        return new OrderItemResponseDto(
                item.getMenu().getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getQuantity()* item.getPrice()
        );
    }
}
