package org.example.delivery.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuCreateRequestDto {

    @NotBlank
    private final String name; // 음식명

    @NotNull
    private final Long price; // 가격

    @NotBlank
    private final String content; // 음식설명

}
