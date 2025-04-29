package org.example.delivery.domain.ad.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindAdResponseDto {
    private Long id;

    private Long storeId;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private int priority;

    private boolean isActive;


}
