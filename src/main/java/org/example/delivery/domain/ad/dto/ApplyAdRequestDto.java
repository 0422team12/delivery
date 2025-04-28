package org.example.delivery.domain.ad.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApplyAdRequestDto {

    private long storeId;

    private int priority;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private boolean isActive;


}
