package org.example.delivery.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.order.repository.OrderRepository;
import org.example.delivery.domain.review.dto.response.ReviewResponseDto;
import org.example.delivery.domain.review.entity.Review;
import org.example.delivery.domain.review.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    //완료처리된 DELIVERED 주문에만 리뷰 생성할 수 있음.
    // 1개의 주문 1개의 리뷰만 가능
    //주문자의 id와 로그인 id 가 같아야함.


    @Transactional
    public ReviewResponseDto createReview(Long userId,Long orderId,int rating,String content){
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "주문자와 작성자가 다릅니다.");
        }

        if (!order.getStatus().equals(Order.Status.DELIVERED)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"해당 주문엔 아직 리뷰를 달 수 없습니다.");

        }
        if(reviewRepository.existsByOrderId(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 리뷰가 존재합니다.");
        }
        Review review = Review.of(order, rating, content);
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findMyReviews(Long userId) {
        List<Review> byUserId = reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if(byUserId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"작성한 리뷰가 없습니다.");
        }
        return byUserId.stream().map(ReviewResponseDto::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findStoreReviews(Long storeId){
        List<Review> byStoreId = reviewRepository.findByStoreIdOrderByCreatedAtDesc(storeId);
        if(byStoreId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"작성된 리뷰가 없습니다.");
        }
        return byStoreId.stream().map(ReviewResponseDto::toDto).toList();
    }

    public List<ReviewResponseDto> findStoreReviewByRating(Long storeId, int minRating, int maxRating) {
        List<Review> byRatingBetween = reviewRepository.findByRatingBetween(minRating, maxRating);
        if(byRatingBetween.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"작성된 리뷰가 없습니다");
        }
        return byRatingBetween.stream().map(ReviewResponseDto::toDto).toList();
    }

    @Transactional
    public void deleteReview(Long userId,Long reviewId){
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }
        reviewRepository.delete(review);
    }

}
