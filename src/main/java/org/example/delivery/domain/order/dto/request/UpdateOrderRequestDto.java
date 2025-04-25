package org.example.delivery.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public class UpdateOrderRequestDto {

    @NotBlank
    private final String status;
}
