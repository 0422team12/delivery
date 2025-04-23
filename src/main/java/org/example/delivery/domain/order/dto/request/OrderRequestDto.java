package org.example.delivery.domain.order.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {

    private final Long cartId;

    private final String address;
}
