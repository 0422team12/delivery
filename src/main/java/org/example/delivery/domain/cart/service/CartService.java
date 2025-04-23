package org.example.delivery.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.cart.dto.request.CreateCartItemRequest;
import org.example.delivery.domain.cart.dto.request.UpdateCartItemRequest;
import org.example.delivery.domain.cart.dto.response.CartItemResponse;
import org.example.delivery.domain.cart.dto.response.CartResponse;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.repository.CartItemRepository;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    //Cart 객체 조회
    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserIdOrElseThrow(userId);
    }

    //장바구니 총 금액 계산
    public Long computeTotalPrice(List<CartItemResponse> items){
        return items.stream().
                mapToLong(CartItemResponse::getPriceSnapshot)
                .sum();
    }

    //장바구니 조회
    public CartResponse getCartInfo(Long userId){
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
    public void addCartItem(Long userId, CreateCartItemRequest createCartItemRequest){

        //유저, 메뉴, 메뉴가 속한 가게 조회
        User user = userRepository.findByIdOrElseThrow(userId);
        Menu menu = menuRepository.findByIdOrElseThrow(createCartItemRequest.getMenuId());
        Store store = menu.getStore();

        // userId에 해당하는 장바구니 존재 여부를 확인하고 없다면 장바구니를 생성한다.
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.createCart(user, store);
                    return cartRepository.save(newCart); //새로 생성해 데이터 베이스에 저장한 newCart 반환
                });

        //장바구니의 가게 id와 메뉴가 속한 가게 id를 비교한다.
        if(!cart.isEqualStoreId(store.getId())){
            //가게 id가 다르다면 장바구니를 초기화한다.
            cart.getCartItems().clear();
            cartRepository.deleteById(cart.getId());
            cart = cartRepository.save(Cart.createCart(user, store));
        }

        //장바구니 만료일시를 업데이트한다.
        cart.updateCartExpriedAt();

        //이미 존재하는 Menu이지 확인한다.
        Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menu.getId());
        if(cartItem.isPresent()){
            //이미 존재하는 Menu라면 기존 수량과 새로운 수량을 더한다.
            CartItem existing = cartItem.get();
            existing.updateQuantity(existing.getQuantity() + createCartItemRequest.getQuantity());
            return ;
        }

        //존재하지 않는 메뉴라면 cartItem에 추가한다.
        cartItemRepository.save(CartItem.createCartItem(cart, menu, createCartItemRequest.getQuantity()));
    }

    //메뉴 수량 변경
    public void updateCartItemQuantity(Long cartItemId, UpdateCartItemRequest updateCartItemRequest){
        //수량이 0 이하일 경우 삭제
        if(updateCartItemRequest.getQuantity() <= 0){
            deleteSingleCartItem(cartItemId);
            return;
        }
        //장바구니의 메뉴 조회
        CartItem cartItem = cartItemRepository.findByIdOrElseThrow(cartItemId);
        //수량을 변경한다.
        cartItem.updateQuantity(updateCartItemRequest.getQuantity());
    }

    //장바구니 단일 메뉴 삭제
    public void deleteSingleCartItem(Long cartItemId){
        cartItemRepository.deleteById(cartItemId);
    }

    //장바구니 전체 삭제
    public void deleteCart(Long userId){
        cartRepository.deleteByUserId(userId);
    }

}
