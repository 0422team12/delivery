package org.example.delivery.domain.menu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuResponseDto {

    private final Long id;

    private final String name;

    private final Long price;

    private final String content;

}
