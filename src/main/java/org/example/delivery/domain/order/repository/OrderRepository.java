package org.example.delivery.domain.order.repository;

import org.example.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByUserId(Long userId);

    Optional<Order> findByUserId(Long userId);
}
