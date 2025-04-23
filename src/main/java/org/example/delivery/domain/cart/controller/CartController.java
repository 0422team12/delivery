package org.example.delivery.domain.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.cart.dto.request.CreateCartItemRequest;
import org.example.delivery.domain.cart.dto.request.UpdateCartItemRequest;
import org.example.delivery.domain.cart.dto.response.CartResponse;
import org.example.delivery.domain.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private CartService cartService;
    //장바구니 메뉴 추가
    @PostMapping("/items")
    public ResponseEntity<Void> addCartItem(@Valid @RequestBody CreateCartItemRequest createCartItemRequest){
        //Token과 연동 예정 임시
        Long userId = 1L;
        cartService.addCartItem(userId, createCartItemRequest);
        return ResponseEntity.ok().build();
    }
    //장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponse> getCartInfo(){
        //Token과 연동 예정 임시
        Long userId = 1L;
        CartResponse cartResponse = cartService.getCartInfo(userId);
        return ResponseEntity.ok(cartResponse);
    }
    //장바구니 메뉴 수량 변경
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest){

        cartService.updateCartItemQuantity(cartItemId, updateCartItemRequest);
        return ResponseEntity.ok().build();
    }
    //장바구니 단일 메뉴 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId){
        cartService.deleteSingleCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }
    //장바구니 전체 삭제
    @DeleteMapping()
    public ResponseEntity<Void> deleteCart(){
        //Token과 연동 예정 임시
        Long userId = 1L;
        cartService.deleteCart(userId);
        return ResponseEntity.ok().build();
    }
}
