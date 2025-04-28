package org.example.delivery.domain.menu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuUpdateRequestDto {

    private final String name; // 음식명

    private final Long price; // 가격

    private final String content; // 음식설명

}