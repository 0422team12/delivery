package org.example.delivery.domain.menu.dto;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.store.entity.Store;

@Getter
@RequiredArgsConstructor
public class CreateMenuRequestDto {

    @NotNull
    private final String name; // 음식명

    @NotNull
    private final Long price; // 가격

    @NotNull
    private final String content; // 음식설명

}
