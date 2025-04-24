package org.example.delivery.domain.review.repository;

import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    boolean existsByOrderId(Long orderId);

    List<Review> findByUserId(Long userId);
    List<Review> findByStoreId(Long storeId);
}
