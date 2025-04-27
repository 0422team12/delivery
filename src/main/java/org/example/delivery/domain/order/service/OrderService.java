package org.example.delivery.domain.order.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.repository.CartItemRepository;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.example.delivery.domain.order.dto.response.FindAllOrderResponseDto;
import org.example.delivery.domain.order.dto.response.OrderResponseDto;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.order.entity.OrderItem;

import org.example.delivery.domain.order.repository.OrderRepository;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderResponseDto createOrder(Long userId/*, Long cartId*/, String address) {
//        User user = userRepository.findById(userId).
//                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        //로그인한 유저라 검증과정 필요없음.

        Cart cart = cartRepository.findByUserId(userId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."));

//        if (!cart.getUser().getId().equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "내 장바구니가 아닙니다.");
//        } 로그인계정으로 찾는거라 검증과정필요없음

        List<CartItem> findAllByCart = cartItemRepository.findCartItemsByCartId(cart.getId());
        if (findAllByCart.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니에 담긴 메뉴가 없습니다..");
        }
        //해당가게가 닫혔는지,영업시간인지 확인
        Store store = cart.getStore();
        LocalTime now = LocalTime.now();
        if (!store.isClosed() && //가게상태 open = false 이고
                now.isAfter(store.getClosingTime()) && //현재시간이 마감시간 이후거나
                now.isBefore(store.getOpeningTime())) { //현지사간이 오픈시간 전이면
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가게가 운영중이지 않습니다.");
        }

        //cartItem - > orderItem / cart_id,menu_id,quantity,price
        List<OrderItem> orderItems = findAllByCart.stream().map(OrderItem::of).collect(Collectors.toList());
        Order order = Order.of(cart, store, orderItems, address);
        orderRepository.save(order);

        log.info("로깅- 요청시각 : {},가게 ID : {},주문 ID : {}",
                LocalDateTime.now(), store.getId(), order.getId());
        //장바구니삭제
        cartRepository.deleteById(cart.getId());

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
        return findAllByUserId.stream().map(FindAllOrderResponseDto::toDto).toList();
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문이 없습니다."));

        if (!order.getUser().getId().equals(userId)) { //로그인 user ,주문 user 일치확인
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 취소할 수 있습니다.");
        }
        if (order.getStatus() != Order.Status.PENDING) { //주문상태 PENDING 일때만 취소가능
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청중인 주문만 취소할 수 있습니다.");
        }
        order.cancel();
    }

    @Transactional
    public OrderResponseDto updateOrder(Long userId, String userRole, Long orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문이 없습니다."));
        if (!UserRole.OWNER.name().equals(userRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "사장님만 주문 상태를 변경할 수 있습니다.");
        }
        if (!order.getStore().getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 가게 주문만 수정할 수 있습니다.");
        }
        try {
            order.updateStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 상태입니다.");
        }

        log.info("로깅- 요청시각 : {},가게 ID : {},주문 ID : {}",
                LocalDateTime.now(), order.getStore().getId(), orderId);

        return OrderResponseDto.toDto(order);
    }
}
