package org.example.delivery.domain.store.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.entity.Menu;

import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class StoreDetailResponseDto {

    private final Long id;

    private final String name; // 가게명

    private final LocalTime openingTime; // 오픈시간

    private final LocalTime closing_time; // 마감시간

    private final Long minOrderValue; // 최소주문금액

    private final List<MenuResponseDto> menuList;

}
