package org.example.delivery.domain.review.repository;

import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    boolean existsByOrderId(Long orderId);
}
