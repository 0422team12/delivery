package org.example.delivery.domain.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.order.dto.request.OrderRequestDto;
import org.example.delivery.domain.order.dto.request.UpdateOrderRequestDto;
import org.example.delivery.domain.order.dto.response.FindAllOrderResponseDto;
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
    public ResponseEntity<OrderResponseDto> createOrder(@RequestAttribute("userId") Long userId,
                                                        @Valid @RequestBody OrderRequestDto dto) {

        OrderResponseDto createOrder = orderService.createOrder(userId/*,dto.getCartId()*/,dto.getAddress());
        return new ResponseEntity<>(createOrder, HttpStatus.CREATED);
    }
    //주문단건조회(본인 주문건만 조회가능)
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrder(@RequestAttribute("userId") Long userId,
                                                      @PathVariable Long orderId){

        OrderResponseDto findOrder = orderService.findOrder(userId, orderId);
        return new ResponseEntity<>(findOrder,HttpStatus.OK);
    }
    //주문전체조회(사용자 id만)
    @GetMapping("/orders")
    public ResponseEntity<List<FindAllOrderResponseDto>> findAllOrders(@RequestAttribute("userId") Long userId){
        List<FindAllOrderResponseDto> findAllOrders = orderService.findAllOrders(userId);
        return new ResponseEntity<>(findAllOrders,HttpStatus.OK);


    }
    //주문취소-order status PENDING 인 경우만 취소 가능
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@RequestAttribute("userId") Long userId,
                                            @PathVariable Long orderId){
        orderService.cancelOrder(userId,orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //주문상태 수정(OWNER)
    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestAttribute("userId") Long userId,
                                                        @RequestAttribute("userRole") String userRole,
                                                        @PathVariable Long orderId,
                                                        @Valid @RequestBody UpdateOrderRequestDto dto){

        OrderResponseDto updateOrder = orderService.updateOrder(userId, userRole, orderId, dto.getStatus());
        return new ResponseEntity<>(updateOrder,HttpStatus.OK);
    }

}
