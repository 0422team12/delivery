package org.example.delivery.domain.cart.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.cart.dto.request.CreateCartItemRequest;
import org.example.delivery.domain.cart.dto.request.UpdateCartItemRequest;
import org.example.delivery.domain.cart.dto.response.CartResponse;
import org.example.delivery.domain.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    //장바구니 메뉴 추가
    @PostMapping("/items")
    public ResponseEntity<Void> addCartItem(
            @Valid @RequestBody CreateCartItemRequest createCartItemRequest,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.addCartItem(userId, createCartItemRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponse> getCartInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        CartResponse cartResponse = cartService.getCartInfo(userId);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    //장바구니 메뉴 수량 변경
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest
    ) {
        System.out.println("메뉴 수량을 수정할게요");
        cartService.updateCartItemQuantity(cartItemId, updateCartItemRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //장바구니 단일 메뉴 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteSingleCartItem(cartItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //장바구니 전체 삭제
    @DeleteMapping()
    public ResponseEntity<Void> deleteCart(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.deleteCart(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
