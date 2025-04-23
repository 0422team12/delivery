package org.example.delivery.domain.order.service;


import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.auth.entity.Auth;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.order.dto.request.OrderRequestDto;
import org.example.delivery.domain.order.dto.response.OrderResponseDto;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.order.repository.OrderRepository;
import org.example.delivery.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto dto) {
        User user = userRepository.findById(userId).
                orElseThrow(()->new NotFoundException("유저를 찾을 수 없습니다."));
        Cart cart = cartRepository.findByUserId(dto.getCartId()).
                orElseThrow(()->new NotFoundException("장바구니를 찾을 수 없습니다."));
        CartItem cartItem = cartItemRepository.findByCartId(cart.getId()).
                orElseThrow(()->new NotFoundException("장바구니에 담긴 메뉴가 없습니다.."));

        Menu menu = cartItem.getMenu();
        int quantity = cartItem.getQuantity();
        int priceEach = cartItem.getPrice_snapshot();

        Order order = Order.of(user, cart.getStore(), menu, quantity, priceEach, dto.getAddress());
        orderRepository.save(order);
        return OrderResponseDto.toDto(order);
    }

}
