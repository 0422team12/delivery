package org.example.delivery.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.store.entity.Store;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class StoreResponseDto {

    private final Long id;

    private final String name; // 가게명

    private final LocalTime openingTime; // 오픈시간

    private final LocalTime closingTime; // 마감시간

    private final Long minOrderValue; // 최소주문금액

    // 팩토리 메서드
    public static StoreResponseDto of(Store store){
        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getMinOrderValue()
        );
    }

}
