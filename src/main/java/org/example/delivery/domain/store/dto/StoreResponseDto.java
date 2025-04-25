package org.example.delivery.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreResponseDto {

    private final Long id;

    private final String name; // 가게명

    private final LocalTime openingTime; // 오픈시간

    private final LocalTime closingTime; // 마감시간

    private final Long minOrderValue; // 최소주문금액

}
