package org.example.delivery.domain.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.user.enums.UserRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @Email(message = "이메일 형식으로 입력해주세요")
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하고 8자 이상이어야 합니다")
    private String password;
    @NotNull
    private UserRole userRole;

}