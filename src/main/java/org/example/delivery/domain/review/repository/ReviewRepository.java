package org.example.delivery.domain.review.repository;

import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    boolean existsByOrderId(Long orderId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Review> findByStoreIdOrderByCreatedAtDesc(Long storeId);

    List<Review> findByStoreIdAndRatingBetween(Long storeId, int ratingAfter, int ratingBefore);

}
