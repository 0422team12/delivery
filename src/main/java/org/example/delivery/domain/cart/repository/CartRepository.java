package org.example.delivery.domain.cart.repository;

import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.exception.CartNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    default Cart findByUserIdOrElseThrow(Long userId) {
        return findByUserId(userId).orElseThrow(CartNotFoundException::new);
    }

    Optional<Cart> findByUserIdAndExpiredAtAfter(Long userId, LocalDateTime expiredAtAfter);

    default Cart findByUserIdAndExpiredAtAfterOrElseThrow(Long userId, LocalDateTime expiredAtAfter) {
        return findByUserIdAndExpiredAtAfter(userId, expiredAtAfter).orElseThrow(CartNotFoundException::new);
    }

    List<Cart> findAllByExpiredAtBefore(LocalDateTime expiredAtBefore);
}
