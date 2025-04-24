package org.example.delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreRequestDto {

    @NotBlank
    private final String name; // 가게명

    @NotBlank
    private final LocalTime openingTime; // 오픈시간, 포맷팅 고민

    @NotBlank
    private final LocalTime closingTime; // 마감시간

    @NotBlank
    private final Long minOrderValue; // 최소주문금액

}
