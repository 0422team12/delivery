package org.example.delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreRequestDto {

    @NotBlank
    private final String name; // 가게명

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private final LocalTime openingTime; // 오픈시간, 포맷팅 고민

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private final LocalTime closingTime; // 마감시간

    @NotNull
    private final Long minOrderValue; // 최소주문금액

}
