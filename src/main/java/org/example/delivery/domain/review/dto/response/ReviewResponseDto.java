package org.example.delivery.domain.review.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.review.entity.Review;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewResponseDto {

    private final Long reviewId; //리뷰 id
    private final String storeName;  //식당이름
    private final int rating;    // 평점
    private final String content;  //리뷰
    private final LocalDateTime createdAt; //리뷰등록일

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final LocalDateTime updatedAt; //리뷰수정일


    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getStore().getName(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
