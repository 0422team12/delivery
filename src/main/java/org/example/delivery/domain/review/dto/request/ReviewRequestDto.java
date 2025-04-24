package org.example.delivery.domain.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "별점은 필수 입니다.")
    @Min(value = 1)
    @Max(value = 5)
    private final Integer rating; //1~5점 정수만허용

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    @Length(min = 5, max = 200)
    private final String content;


}
