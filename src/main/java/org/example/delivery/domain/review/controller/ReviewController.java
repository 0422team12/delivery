package org.example.delivery.domain.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestAttribute("userId") Long userId,
                                                          @PathVariable Long orderId,
                                                          @Valid @RequestBody ReviewRequestDto dto) {

        ReviewResponseDto createReview = reviewService.createReview(userId, orderId, dto.getRating(), dto.getContent());
        return new ResponseEntity<>(createReview, HttpStatus.CREATED);
    }
    //내가 작성한 리뷰보기
    @GetMapping("users/me/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findMyReviews(@RequestAttribute("userId") Long userId){
        List<ReviewResponseDto> findMyReviews = reviewService.findMyReviews(userId);
        return new ResponseEntity<>(findMyReviews,HttpStatus.OK);
    }

    //가게 리뷰보기
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findStoreReviews(@PathVariable Long storeId){
        List<ReviewResponseDto> findStoreReviews = reviewService.findStoreReviews(storeId);
        return new ResponseEntity<>(findStoreReviews,HttpStatus.OK);
    }
    //가게 리뷰 평점별로조회
    @GetMapping(value = "/stores/{storeId}/reviews", params = {"minRating", "maxRating"})
    public ResponseEntity<List<ReviewResponseDto>> findStoreReviewByRating(@PathVariable Long storeId,
                                                                           @RequestParam int minRating,
                                                                           @RequestParam int maxRating){
        List<ReviewResponseDto> storeReviewByRating = reviewService.findStoreReviewByRating(storeId, minRating, maxRating);
        return new ResponseEntity<>(storeReviewByRating,HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@RequestAttribute("userId")Long userId,
                                             @PathVariable Long reviewId){
        reviewService.deleteReview(userId, reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
