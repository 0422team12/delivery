package org.example.delivery.domain.cart.repository;

import org.example.delivery.domain.cart.dto.response.CartItemResponse;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    default CartItem findByIdOrElseThrow(Long cartId){
        return findById(cartId).orElseThrow();
    }

    //DTO Projection
    @Query("""
            SELECT new org.example.delivery.domain.cart.dto.response.CartItemResponse (
                ci.id, m.id, m.name, ci.quantity, ci.priceSnapshot
            )
            FROM CartItem ci
            JOIN ci.menu m
            WHERE ci.cart.id = :cartId
    """)
    List<CartItemResponse> findAllByCartId(@Param("cartId") Long cartId);

    Optional<CartItem> findByCartIdAndMenuId(Long cartId, Long menuId);

    Long menu(Menu menu);
}
