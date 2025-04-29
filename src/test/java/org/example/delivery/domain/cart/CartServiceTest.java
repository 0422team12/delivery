package org.example.delivery.domain.cart;


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
import org.example.delivery.domain.menu.repository.MenuRepository;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.repository.StoreRepository;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    //자주 사용되는 변수
    @Mock
    private User mockUser;
    @Mock
    private Store mockStore1;
    @Mock
    private Store mockStore2;
    @Mock
    private Menu mockMenu1;
    @Mock
    private Menu mockMenu2;


    @BeforeEach
    void setUp(){
        //자주 쓰이는 부분을 넣어놓았으나 쓰이지 않는 경우 경고가 뜨기 때문에 lenient를 붙여 필요할때만 쓰라고 명시
        lenient().when(mockUser.getId()).thenReturn(1L);

        lenient().when(mockStore1.getId()).thenReturn(1L);
        lenient().when(mockStore1.isClosed()).thenReturn(false);
        lenient().when(mockStore1.getMinOrderValue()).thenReturn(10000L);

        lenient().when(mockMenu1.getId()).thenReturn(1L);
        lenient().when(mockMenu1.getPrice()).thenReturn(15000L);
        lenient().when(mockMenu1.getStore()).thenReturn(mockStore1);

        lenient().when(mockStore2.getId()).thenReturn(2L);
        lenient().when(mockStore2.isClosed()).thenReturn(false);
        lenient().when(mockStore2.getMinOrderValue()).thenReturn(10000L);

        lenient().when(mockMenu2.getId()).thenReturn(2L);
        lenient().when(mockMenu2.getPrice()).thenReturn(15000L);
        lenient().when(mockMenu2.getStore()).thenReturn(mockStore2);
    }

    @Test
    @DisplayName("빈 장바구니에 메뉴 추가 시, 장바구니가 생성된다")
    void testAddItemCreatesCart() {
        //given
        int quantity = 2;
        CreateCartItemRequest createCartItemRequest = new CreateCartItemRequest(mockMenu1.getId(), quantity);

        given(userRepository.findById(mockUser.getId())).willReturn(Optional.of(mockUser));
        given(menuRepository.findById(mockMenu1.getId())).willReturn(Optional.of(mockMenu1));
        given(cartRepository.findByUserId(mockUser.getId())).willReturn(Optional.empty()); // 기존에는 cart 없음
        given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

        //when
        cartService.addCartItem(mockUser.getId(), createCartItemRequest);

        //then 새로운 장바구니가 저장되는가, 장바구니에 메뉴가 제대로 들어가는가
        verify(cartRepository).save(any(Cart.class));
        verify(cartItemRepository).save(argThat(cartItem ->
                cartItem.getMenu().getId().equals(mockMenu1.getId()) &&
                        cartItem.getQuantity() == quantity
        ));
    }

    @Test
    @DisplayName("다른 가게의 메뉴를 고를 경우, 장바구니가 초기화 되고 새로운 메뉴가 담긴다.")
    void testInitNewCart(){
        //given
        int quantity = 1;
        CreateCartItemRequest request = new CreateCartItemRequest(mockMenu2.getId(), quantity);

        Cart existingCart = mock(Cart.class);
        when(existingCart.getId()).thenReturn(1L);
        when(existingCart.isExpired()).thenReturn(false);
        when(existingCart.isEqualStoreId(anyLong())).thenReturn(false);

        // stubbing
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(existingCart));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(menuRepository.findById(mockMenu2.getId())).thenReturn(Optional.of(mockMenu2));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.findByUserIdOrElseThrow(mockUser.getId())).thenReturn(existingCart);

        //when
        cartService.addCartItem(mockUser.getId(), request);

        //then 기존 장바구니가 삭제되고 새로운 장바구니가 저장되는가, 장바구니에 메뉴가 제대로 들어가는가
        verify(cartRepository).deleteById(existingCart.getId());
        verify(cartRepository).save(any(Cart.class));
        verify(cartItemRepository).save(argThat(cartItem ->
                cartItem.getMenu().getId().equals(mockMenu2.getId()) &&
                        cartItem.getQuantity() == quantity
        ));
    }

    @Test
    @DisplayName("동일한 메뉴 추가 시, 수량이 증가한다.")
    void testAddEqualItemIncreasCartItemQuantity() {
        // given
        int initQuantity = 2;
        int addQuantity = 3;
        CreateCartItemRequest request = new CreateCartItemRequest(mockMenu1.getId(), addQuantity);

        Cart existingCart = Cart.createCart(mockUser, mockStore1, LocalDateTime.now().plusDays(1));
        CartItem existingCartItem = CartItem.createCartItem(existingCart, mockMenu1, initQuantity);
        existingCart.getCartItems().add(existingCartItem);

        // stubbing
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(menuRepository.findById(mockMenu1.getId())).thenReturn(Optional.of(mockMenu1));
        when(cartRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(existingCart));
        when(cartItemRepository.findByCartIdAndMenuId(existingCart.getId(), mockMenu1.getId()))
                .thenReturn(Optional.of(existingCartItem));

        // when
        cartService.addCartItem(mockUser.getId(), request);

        // then
        assertThat(existingCart.getCartItems()).hasSize(1); // 메뉴 종류는 여전히 1개
        assertThat(existingCartItem.getQuantity()).isEqualTo(initQuantity + addQuantity); // 수량이 합산되어야 한다.
    }

    @Test
    @DisplayName("수량이 정상적으로 수정된다.")
    void testUpdateCartItemQuantity(){
        //given
        Long cartItemId = 1L;
        int newQuantity = 1;

        Cart cart = Cart.createCart(mockUser, mockStore1, LocalDateTime.now().plusDays(1));
        CartItem cartItem = CartItem.createCartItem(cart, mockMenu1, 2); // 기존 수량은 2

        when(cartItemRepository.findByIdOrElseThrow(cartItemId)).thenReturn(cartItem);

        UpdateCartItemRequest request = new UpdateCartItemRequest(newQuantity);

        // when
        cartService.updateCartItemQuantity(cartItemId, request);

        // then
        assertThat(cartItem.getQuantity()).isEqualTo(newQuantity); // 수량이 바뀌었는지 검증
    }

    @Test
    @DisplayName("수량이 0인 경우 해당 아이템을 삭제한다.")
    void testUpdateCartItemQuantityZero() {
        //given
        Long cartItemId = 1L;
        int quantity = 0;

        Cart mockCart = mock(Cart.class);
        CartItem mockCartItem = mock(CartItem.class);

        // cartItemRepository가 cartItemId로 찾아오면 mockCartItem을 리턴하도록 stubbing
        when(cartItemRepository.findByIdOrElseThrow(cartItemId)).thenReturn(mockCartItem);

        // cartItem.getCart() 호출 시 mockCart 리턴
        when(mockCartItem.getCart()).thenReturn(mockCart);

        UpdateCartItemRequest request = new UpdateCartItemRequest(quantity);

        // when
        cartService.updateCartItemQuantity(cartItemId, request);

        // then
        verify(cartItemRepository).deleteById(cartItemId);
    }

//    대충 테스트 코드가 이런 흐름이면 되겠다 라는 생각으로 적어놓은 테스트 코드
//    (실제 성공적으로 동작하지는 않음)
//    @Test
//    @DisplayName("장바구니 전체 삭제 시, Cart와 연관된 cartItem은 삭제된다.")
//    void testDeleteCartItemsWithCart(){
//        //given
//        Long userId = 1L;
//        CreateCartItemRequest createCartItemRequest1 = new CreateCartItemRequest(1L, 1);
//        CreateCartItemRequest createCartItemRequest2 = new CreateCartItemRequest(2L, 1);
//        cartService.addCartItem(userId, createCartItemRequest1);
//        cartService.addCartItem(userId, createCartItemRequest2);
//        Long cartId = cartService.getCartByUserId(userId).getId();
//
//        //when
//        cartService.deleteCart(userId);
//
//        //then
//        assertThat(cartItemRepository.findAllByCartId(cartId).size()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("장바구니 전체 삭제 시, Cart는 조회되지 않는다.")
//    void testDeleteCart(){
//        //given
//        Long userId = 1L;
//        CreateCartItemRequest createCartItemRequest = new CreateCartItemRequest(1L, 1);
//        cartService.addCartItem(userId, createCartItemRequest);
//
//        //when
//        cartService.deleteCart(userId);
//
//        //then
//        assertThrows(IllegalArgumentException.class, () -> {
//            cartService.getCartByUserId(userId);
//        });
//    }
//
//    @Test
//    @DisplayName("만료된 장바구니는 조회되지 않는다.")
//    void testUnvalidCartCouldNotFind(){
//        //given
//        Cart cart = Cart.createCart(new User(), new Store(), LocalDateTime.now().plusDays(1));
//        cartRepository.save(cart);
//        //when
//        cartRepository.findByUserIdAndExpiredAtAfterOrElseThrow(userId, LocalDateTime.now().plusDays(2));
//        //then
//        //조회되는게 없어서 throw 되어야 함
//    }
}
