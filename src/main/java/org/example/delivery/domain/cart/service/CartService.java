package org.example.delivery.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.cart.dto.request.CreateCartItemRequest;
import org.example.delivery.domain.cart.dto.request.UpdateCartItemRequest;
import org.example.delivery.domain.cart.dto.response.CartItemResponse;
import org.example.delivery.domain.cart.dto.response.CartResponse;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.exception.CartNotFoundException;
import org.example.delivery.domain.cart.repository.CartItemRepository;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.menu.repository.MenuRepository;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    //Cart 객체 조회 - 만료된 객체는 조회되지 않음
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserIdAndExpiredAtAfterOrElseThrow(userId, LocalDateTime.now());
    }

    //장바구니 총 금액 계산
    private Long computeTotalPrice(List<CartItemResponse> items) {
        return items.stream().
                mapToLong(item -> item.getPriceSnapshot() * item.getQuantity())
                .sum();
    }

    //장바구니 조회
    public CartResponse getCartInfo(Long userId) {
        Cart cart = getCartByUserId(userId);
        //가게 정보 가져오기
        Store store = cart.getStore();
        //장바구니에 담긴 메뉴 가져오기
        List<CartItemResponse> cartItemResponseList = cartItemRepository.findAllByCartId(cart.getId());
        //합계 계산
        Long totalPrice = computeTotalPrice(cartItemResponseList);
        //최소 주문 충족 여부
        boolean isOverMinOrderValue = store.isOverMinOrderValue(totalPrice);
        return CartResponse.createCartResponse(cart.getId(), store, cartItemResponseList, totalPrice, isOverMinOrderValue);
    }

    //장바구니에 메뉴 추가
    public void addCartItem(Long userId, CreateCartItemRequest createCartItemRequest) {

        //유저, 메뉴, 메뉴가 속한 가게 조회
        User user = userRepository.findById(userId).orElseThrow();
        Menu menu = menuRepository.findById(createCartItemRequest.getMenuId()).orElseThrow();
        Store store = menu.getStore();

        // userId에 해당하는 장바구니 존재 여부를 확인한다.
        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        //존재하는 장바구니가 없다면 새로운 장바구니를 생성한다.
        if (cart == null) {
            cart = cartRepository.save(Cart.createCart(user, store, LocalDateTime.now().plusDays(1)));
        }

        //존재하는 장바구니가 이미 만료되었거나, 장바구니의 가게 id와 메뉴가 속한 가게 id가 다른 경우 초기화한다.
        if (cart.isExpired() || !cart.isEqualStoreId(store.getId())) {
            deleteCart(userId);
            cartRepository.flush(); //삭제를 즉시 반영
            cart = cartRepository.save(Cart.createCart(user, store, LocalDateTime.now().plusDays(1)));
        }

        //이미 존재하는 Menu이지 확인한다.
        Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menu.getId());
        if (cartItem.isPresent()) {
            //이미 존재하는 Menu라면 기존 수량과 새로운 수량을 더한다.
            CartItem existing = cartItem.get();
            existing.updateQuantity(existing.getQuantity() + createCartItemRequest.getQuantity());
            return;
        }

        //장바구니 만료일시를 업데이트한다.
        cart.updateCartExpiredAt();

        //존재하지 않는 메뉴라면 cartItem에 추가한다.
        cartItemRepository.save(CartItem.createCartItem(cart, menu, createCartItemRequest.getQuantity()));
    }

    //Cart 객체의 유효성 검사
    private void validateCartIsExpired(Cart cart) {
        if (cart.isExpired()) {
            throw new CartNotFoundException();
        }
    }

    //메뉴 수량 변경
    public void updateCartItemQuantity(Long cartItemId, UpdateCartItemRequest updateCartItemRequest) {

        //장바구니의 메뉴 조회
        CartItem cartItem = cartItemRepository.findByIdOrElseThrow(cartItemId);

        //장바구니 유효성 검사
        validateCartIsExpired(cartItem.getCart());

        //만료시간 업데이트
        cartItem.getCart().updateCartExpiredAt();

        //수량이 0 이하일 경우 삭제
        if (updateCartItemRequest.getQuantity() <= 0) {
            deleteSingleCartItem(cartItemId);
            return;
        }

        //수량을 변경한다.
        cartItem.updateQuantity(updateCartItemRequest.getQuantity());
    }

    //장바구니 단일 메뉴 삭제
    public void deleteSingleCartItem(Long cartItemId) {
        //장바구니의 메뉴 조회
        CartItem cartItem = cartItemRepository.findByIdOrElseThrow(cartItemId);
        //장바구니 유효성 검사
        validateCartIsExpired(cartItem.getCart());

        cartItemRepository.deleteById(cartItemId);
    }

    //장바구니 삭제
    public void deleteCart(Long userId) {
        //장바구니 조회
        Cart cart = cartRepository.findByUserIdOrElseThrow(userId);
        //장바구니 유효성 검사
        validateCartIsExpired(cart);

        cartRepository.deleteById(cart.getId());
    }

}
