package org.example.delivery.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.domain.order.entity.OrderItem;

@Getter
@AllArgsConstructor
public class OrderItemResponseDto {
    private final Long itemId;
    private final String menuName;
    private final int quantity;
    private final int price;
    private final int totalPrice;

    public static OrderItemResponseDto toDto(OrderItem item){
        return new OrderItemResponseDto(
                item.getId(),
                item.getMenuName(),
                item.getQuantity(),
                item.getPrice(),
                item.getQuantity()* item.getPrice()
        );
    }
}
