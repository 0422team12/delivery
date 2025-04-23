package org.example.delivery.domain.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.order.dto.request.OrderRequestDto;
import org.example.delivery.domain.order.dto.response.OrderResponseDto;
import org.example.delivery.domain.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    //주문생성
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(HttpServletRequest request,
                                                        @RequestBody OrderRequestDto dto) {

        Long userId = (Long) request.getAttribute("userId");
        OrderResponseDto createOrder = orderService.createOrder(userId, dto);
        return new ResponseEntity<>(createOrder, HttpStatus.CREATED);
    }
    //주문단건조회(본인 주문건만 조회가능)
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> findOrder(HttpServletRequest request,
                                                     @PathVariable Long orderId){

        Long userId = (Long) request.getAttribute("userId");
        OrderResponseDto findOrder = orderService.findOrder(userId, orderId);
        return new ResponseEntity<>(findOrder,HttpStatus.OK);
    }


}
