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
    private String name;

    @NotNull
    private Long price;

    @NotNull
    private String content;

}
