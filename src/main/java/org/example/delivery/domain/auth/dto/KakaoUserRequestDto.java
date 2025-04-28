package org.example.delivery.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.delivery.domain.user.enums.UserRole;

@Getter
public class KakaoUserRequestDto {
    @NotNull
    private UserRole userRole;
}
