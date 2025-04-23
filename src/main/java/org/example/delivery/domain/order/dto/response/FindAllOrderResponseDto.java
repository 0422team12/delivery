package org.example.delivery.domain.order.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.order.entity.Order;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FindAllOrderResponseDto {

    private final Long orderId;
    private final String storeName;
    private final String menuName;
    private final LocalDateTime orderedAt;

    public static FindAllOrderResponseDto toDto(Order order){
        return new FindAllOrderResponseDto(
                order.getId(),
                order.getStore().getName(),
                order.getMenu().getName(),
                order.getOrderedAt()
        );
    }
}
