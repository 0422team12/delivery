package org.example.delivery.domain.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class CreateStoreRequestDto {

    @NotNull
    private final String name; // 가게명

    @NotNull
    private final LocalTime openingTime; // 오픈시간, 포맷팅 고민

    @NotNull
    private final LocalTime closing_time; // 마감시간

    @NotNull
    private final Long minOrderValue; // 최소주문금액

}
