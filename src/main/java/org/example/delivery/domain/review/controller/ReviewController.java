package org.example.delivery.domain.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.delivery.domain.review.dto.request.ReviewRequestDto;
import org.example.delivery.domain.review.dto.response.ReviewResponseDto;
import org.example.delivery.domain.review.entity.Review;
import org.example.delivery.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/review")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestAttribute("userId") Long userId,
                                                          @PathVariable Long orderId,
                                                          @RequestBody ReviewRequestDto dto) {

        ReviewResponseDto createReview = reviewService.createReview(userId, orderId, dto.getRating(), dto.getContent());
        return new ResponseEntity<>(createReview, HttpStatus.CREATED);
    }
    //내가 작성한 리뷰보기
    @GetMapping("user/me/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findMyReviews(@RequestAttribute("userId") Long userId){
        List<ReviewResponseDto> findMyReviews = reviewService.findMyReviews(userId);
        return new ResponseEntity<>(findMyReviews,HttpStatus.OK);
    }

    //가게 리뷰보기
    @GetMapping("/store/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findStoreReviews(@PathVariable Long storeId){
        List<ReviewResponseDto> findStoreReviews = reviewService.findStoreReviews(storeId);
        return new ResponseEntity<>(findStoreReviews,HttpStatus.OK);
    }

}
