package org.example.delivery.cart;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.example.delivery.domain.cart.dto.request.CreateCartItemRequest;
import org.example.delivery.domain.cart.dto.request.UpdateCartItemRequest;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.repository.CartItemRepository;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.example.delivery.domain.cart.service.CartService;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CartServiceTest {

    private CartService cartService;
    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;

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
        assertThat(cart.getCartItems()).hasSize(1);
    }

    @Test
    @DisplayName("다른 가게의 메뉴를 고를 경우, 장바구니가 초기화 되고 새로운 메뉴가 담긴다.")
    void testInitNewCart(){
        //given
        Long userId = 1L;
        Long menuId = 1L;
        int quantity = 2;
        CreateCartItemRequest createCartItemRequest1 = new CreateCartItemRequest(menuId, quantity);

        Long menuId2 = 3L; //다른 가게 메뉴
        int quantity2 = 2;
        CreateCartItemRequest createCartItemRequest2 = new CreateCartItemRequest(menuId2, quantity2);

        cartService.addCartItem(userId, createCartItemRequest1);
        Long originCartId = cartService.getCartByUserId(userId).getId();

        //when
        cartService.addCartItem(userId, createCartItemRequest2);
        //then
        Cart cart = cartService.getCartByUserId(userId);
        assertThat(cart.getId()).isNotEqualTo(originCartId);
        assertThat(cart.getCartItems().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("동일한 메뉴 추가 시, 수량이 증가한다.")
    void testAddEqualItemIncreasCartItemQuantity() {
        //given
        Long userId = 1L;
        Long menuId = 1L;
        int initQuantity = 2;
        int quantity = 3;

        CreateCartItemRequest createCartItemRequest1 = new CreateCartItemRequest(menuId, quantity);
        CreateCartItemRequest createCartItemRequest2 = new CreateCartItemRequest(menuId, quantity);

        cartService.addCartItem(userId, createCartItemRequest1);

        //when
        cartService.addCartItem(userId, createCartItemRequest2);

        //then
        Cart cart = cartService.getCartByUserId(userId);
        assertThat(cart.getCartItems()).hasSize(1);  //담긴 메뉴의 종류는 1개여야 한다.

        CartItem cartItem = cart.getCartItems().get(0);
        assertThat(cartItem.getQuantity()).isEqualTo(initQuantity + quantity); //메뉴의 수량이 initQuantity + quantity이어야 한다.
    }

    @Test
    @DisplayName("수량이 정상적으로 수정된다.")
    void testUpdateCartItemQuantity(){
        //given
        Long userId = 1L;
        Long menuId = 1L;
        int initQuantity = 1;
        int updateQuantity = 1;

        CreateCartItemRequest createCartItemRequest = new CreateCartItemRequest(menuId, initQuantity);
        cartService.addCartItem(userId, createCartItemRequest);

        Long cartItemId = cartItemRepository.findByCartIdAndMenuId(cartService.getCartByUserId(userId).getId(), menuId).get().getId();
        UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest(updateQuantity);

        //when
        cartService.updateCartItemQuantity(cartItemId, updateCartItemRequest);

        //then
        CartItem cartItem = cartItemRepository.findByIdOrElseThrow(cartItemId);
        assertThat(cartItem.getQuantity()).isEqualTo(updateQuantity);
    }

    @Test
    @DisplayName("수량이 0인 경우 해당 아이템을 삭제한다.")
    void testUpdateCartItemQuantityZero(){
        //given
        Long cartItemId = 1L;
        int quantity = 0;

        UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest(quantity);

        //when
        cartService.updateCartItemQuantity(cartItemId, updateCartItemRequest);

        //then
        assertThrows(IllegalArgumentException.class, ()-> { CartItem cartItem = cartItemRepository.findByIdOrElseThrow(cartItemId); });
    }

    @Test
    @DisplayName("장바구니 단일 메뉴 삭제 시, 나머지는 유지된다.")
    void testDeleteSingleCartItem(){
        //given
        Long userId = 1L;
        Long menuId1 = 1L;
        Long menuId2 = 1L;
        CreateCartItemRequest createCartItemRequest1 = new CreateCartItemRequest(menuId1, 1);
        CreateCartItemRequest createCartItemRequest2 = new CreateCartItemRequest(menuId2, 2);
        cartService.addCartItem(userId, createCartItemRequest1);
        cartService.addCartItem(userId, createCartItemRequest2);
        Cart cart = cartService.getCartByUserId(userId);

        //when
        cartService.deleteSingleCartItem(cart.getCartItems().get(0).getId());

        //then
        assertThat(cart.getCartItems().size()).isEqualTo(1); //2개의 메뉴 중 1개만 삭제했으니 크기는 장바구니 메뉴의 수는 1개여야 한다.
    }

    @Test
    @DisplayName("장바구니 전체 삭제 시, Cart와 연관된 cartItem은 삭제된다.")
    void testDeleteCartItemsWithCart(){
        //given
        Long userId = 1L;
        CreateCartItemRequest createCartItemRequest1 = new CreateCartItemRequest(1L, 1);
        CreateCartItemRequest createCartItemRequest2 = new CreateCartItemRequest(2L, 1);
        cartService.addCartItem(userId, createCartItemRequest1);
        cartService.addCartItem(userId, createCartItemRequest2);
        Long cartId = cartService.getCartByUserId(userId).getId();

        //when
        cartService.deleteCart(userId);

        //then
        assertThat(cartItemRepository.findAllByCartId(cartId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("장바구니 전체 삭제 시, Cart는 조회되지 않는다.")
    void testDeleteCart(){
        //given
        Long userId = 1L;
        CreateCartItemRequest createCartItemRequest = new CreateCartItemRequest(1L, 1);
        cartService.addCartItem(userId, createCartItemRequest);

        //when
        cartService.deleteCart(userId);

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartByUserId(userId);
        });
    }

    @Test
    @DisplayName("만료된 장바구니는 조회되지 않는다.")
    void testUnvalidCartCouldNotFind(){
        //given
        Cart cart = Cart.createCart(new User(), new Store());
        cart.updateCartExpriedAt();
        cartRepository.save(cart);
        //when
        cartRepository.findByUserIdAndExpiredAtAfterOrElseThrow(userId, LocalDateTime.now().plusDays(2));
        //then
        //조회되는게 없어서 throw 되어야 함
    }
}
