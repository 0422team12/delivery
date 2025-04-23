package org.example.delivery.cart;


import org.assertj.core.api.Assertions;
import org.example.delivery.domain.cart.dto.request.CreateCartItemRequest;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartServiceTest {

    CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
    }

    @Test
    @DisplayName("빈 장바구니에 메뉴 추가 시, 장바구니가 생성된다")
    void testAddItemCreatesCart() {
        //given
        Long userId = 1L;
        Long menuId = 1L;
        int quantity = 2;
        CreateCartItemRequest createCartItemRequest = new CreateCartItemRequest(menuId, quantity);

        //when
        cartService.addCartItem(userId, createCartItemRequest);

        //then
        Cart cart = cartService.getCartByUserId(userId);
        Assertions.assertThat(cart.getCartItems()).hasSize(1);
    }

    @Test
    @DisplayName("동일한 메뉴 추가 시, 수량이 증가한다.")
    void testAddEqualItemIncreasCartItemQuantity() {
        Long userId = 1L;
        Long menuId = 1L;
        int initQuantity = 2;
        int quantity = 3;

        CreateCartItemRequest createCartItemRequest1 = new CreateCartItemRequest(menuId, quantity);
        CreateCartItemRequest createCartItemRequest2 = new CreateCartItemRequest(menuId, quantity);

        cartService.addCartItem(userId, createCartItemRequest1);
        //동일한 메뉴 추가 시도
        cartService.addCartItem(userId, createCartItemRequest2);

        //담긴 메뉴의 종류는 1개여야 한다.
        Cart cart = cartService.getCartByUserId(userId);
        Assertions.assertThat(cart.getCartItems()).hasSize(1);

        //메뉴의 수량이 initQuantity + quantity이어야 한다.
        CartItem cartItem = cart.getCartItems().get(0);
        Assertions.assertThat(cartItem.getQuantity()).isEqualTo(initQuantity + quantity);
    }

//    @Test
//    @DisplayName("수량이 정상적으로 수정된다.")
//    void testUpdateCartItemQuantity(){
//
//    }
//
//    @Test
//    @DisplayName("장바구니 단일 메뉴 삭제 시, 나머지는 유지된다.")
//    void testDeleteSingleCartItem(){
//
//    }
//
//    @Test
//    @DisplayName("장바구니 전체 삭제 시, 모든 CartItem이 제거된다.")
//    void testDeleteCart(){
//
//    }
}
