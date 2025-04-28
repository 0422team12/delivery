package org.example.delivery.domain.menu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;

@Getter
@RequiredArgsConstructor
public class MenuResponseDto {

    private final Long id;

    private final String name;

    private final Long price;

    private final String content;

    // 팩토리 메서드
    public static MenuResponseDto of(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getContent()
        );
    }

}
