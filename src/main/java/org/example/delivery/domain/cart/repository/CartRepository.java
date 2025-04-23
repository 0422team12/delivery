package org.example.delivery.domain.cart.repository;

import org.example.delivery.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    default Cart findByUserIdOrElseThrow(Long userId){
        //예외 통합 후 추가 예정
        return findByUserId(userId).orElseThrow();
    }

    void deleteByUserId(Long userId);

}
