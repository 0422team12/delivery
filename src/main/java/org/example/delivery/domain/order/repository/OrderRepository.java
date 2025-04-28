package org.example.delivery.domain.order.repository;

import org.example.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByUserId(Long userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItem WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

}
