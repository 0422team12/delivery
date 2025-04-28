package org.example.delivery.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreUpdateRequestDto {

    private final String name; // 가게명

    @DateTimeFormat(pattern = "HH:mm")
    private final LocalTime openingTime; // 오픈시간, 포맷팅 고민

    @DateTimeFormat(pattern = "HH:mm")
    private final LocalTime closingTime; // 마감시간

    private final Long minOrderValue; // 최소주문금액

}
