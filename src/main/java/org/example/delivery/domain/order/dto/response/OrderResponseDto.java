package org.example.delivery.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private final Long orderId; //주문번호
    private final Long userId;  //주문자 ID
    private final Long storeId; //식당 ID
    private final String status;

    private final List<OrderItemResponseDto> items;
    private final int totalPrice;

    private final String address;
    private final LocalDateTime orderedAt;
    private final LocalDateTime deliveredAt;



    public static OrderResponseDto toDto(Order order){
        return new OrderResponseDto(
                order.getId(), //주문번호
                order.getUser().getId(), //user id
                order.getStore().getId(),//가게 id
                order.getStatus().name(),  //배달상태
                order.getOrderItem().stream().map(OrderItemResponseDto::toDto).toList(),
                order.getTotalPrice(),  //총액
                order.getAddress(),        //주소
                order.getOrderedAt(),     //주문시간
                order.getDeliveredAt()    //배달완료시간
        );
    }
}
