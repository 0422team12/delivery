package org.example.delivery.domain.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.delivery.domain.review.dto.request.ReviewRequestDto;
import org.example.delivery.domain.review.dto.response.ReviewResponseDto;
import org.example.delivery.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/review")
    public ResponseEntity<ReviewResponseDto>createReview(@RequestAttribute("userId")Long userId,
                                                         @PathVariable Long orderId,
                                                         @RequestBody ReviewRequestDto dto){

        reviewService.createReview(userId,orderId,dto.getRating(),dto.getContent());
    }
}
