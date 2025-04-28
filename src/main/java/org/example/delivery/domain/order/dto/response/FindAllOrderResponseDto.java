package org.example.delivery.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.domain.order.entity.Order;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class FindAllOrderResponseDto {

    private final Long orderId;
    private final Long storeId;
    private final String storeName;
    private final String menuName;
    private final int totalPrice;
    private final String status;
    private final LocalDateTime orderedAt;

    public static FindAllOrderResponseDto toDto(Order order){
        return new FindAllOrderResponseDto(
                order.getId(),
                order.getStore().getId(),
                order.getStore().getName(),
                order.getOrderItem().stream().map(item->item.getMenuName()).toList().toString(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getOrderedAt()
        );
    }
}
