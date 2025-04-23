package org.example.delivery.domain.order.service;


import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.order.dto.request.OrderRequestDto;
import org.example.delivery.domain.order.dto.response.FindAllOrderResponseDto;
import org.example.delivery.domain.order.dto.response.OrderResponseDto;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.order.repository.OrderRepository;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

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
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        Cart cart = cartRepository.findByUserId(dto.getCartId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."));
        CartItem cartItem = cartItemRepository.findByCartId(cart.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니에 담긴 메뉴가 없습니다.."));

        Menu menu = cartItem.getMenu();
        int quantity = cartItem.getQuantity();
        int priceEach = cartItem.getPrice_snapshot();

        Order order = Order.of(user, cart.getStore(), menu, quantity, priceEach, dto.getAddress());
        orderRepository.save(order);
        return OrderResponseDto.toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."));
        if (!order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 주문만 조회할 수 있습니다.");
        }
        return OrderResponseDto.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<FindAllOrderResponseDto> findAllOrders(Long userID) {
        List<Order> findAllByUserId = orderRepository.findAllByUserId(userID);
        if (findAllByUserId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문한 이력이 없습니다.");
        }
        return findAllByUserId.stream().map(FindAllOrderResponseDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문이 없습니다."));

        if(order.getStatus() != Order.Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청중인 주문만 취소할 수 있습니다.");
        }
        if (!order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 취소할 수 있습니다.");
        }
        order.cancel();
    }
}
