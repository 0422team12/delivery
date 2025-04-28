package org.example.delivery;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.repository.CartItemRepository;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.example.delivery.domain.order.repository.OrderRepository;
import org.example.delivery.domain.order.service.OrderService;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    CartRepository cartRepository;
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    User user1 = new User("형철@naver.com", "123456789!A", UserRole.USER);
    User user2 = new User("형진@naver.com", "123456789!A", UserRole.OWNER);

    String str1 = "서울 강남구";
    String str2 = "대전 유성구";


    @Test
    void 장바구니가_없으면_생성시_NOT_FOUND가_발생한다() {

        //given
        given(cartRepository.findByUserId(user1.getId()))
                .willReturn(Optional.empty());

        //when
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> orderService.createOrder(user1.getId(), str1)
        );
        //then
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("장바구니를 찾을 수 없습니다.", ex.getReason());
    }


    @Test
    void 생성시_장바구니에_아무것도_담겨있지않다면_NOT_FOUND가_발생한다() {
        //given
        Cart cart1 = cartRepository.save(Cart.createCart(user1, null, null));
        CartItem cartItem1 = CartItem.createCartItem(cart1, null, 1);
        given(cartItemRepository.findAllByCartId(cartItem1.getId())).willReturn(List.of());

        //when
        ResponseStatusException ex =assertThrows(
                ResponseStatusException.class,
                () -> orderService.createOrder(user1.getId(), str1));

        //then
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());;
        assertEquals("장바구니에 담긴 메뉴가 없습니다..",ex.getMessage());
    }
}
