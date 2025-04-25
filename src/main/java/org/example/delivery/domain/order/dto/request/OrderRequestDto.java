package org.example.delivery.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {

//    @NotNull
//    private final Long cartId;

    @NotBlank
    private final String address;
}
