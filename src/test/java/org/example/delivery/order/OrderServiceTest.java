package org.example.delivery.order;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.entity.CartItem;
import org.example.delivery.domain.cart.repository.CartItemRepository;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.order.dto.response.OrderResponseDto;
import org.example.delivery.domain.order.entity.Order;
import org.example.delivery.domain.order.repository.OrderRepository;
import org.example.delivery.domain.order.service.OrderService;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    void 주문이_정상적으로_생성된다(){
        //given
        User user = new User("형철@naver.com","123456789!A", UserRole.OWNER);
        Store store = new Store(user,"맛있는 삼겹살", LocalTime.of(9,0),LocalTime.of(18,0),10000L);
        Menu menu =new Menu(store,"삼겹살",10000L,"언제먹어도 맛있는 삼겹살",false);

        Cart cart = Cart.createCart(user, store, LocalDateTime.now().plusHours(1));

        CartItem cartItem = CartItem.createCartItem(cart, menu, 3);
        //when
        when(cartRepository.findByUserIdOrElseThrow(user.getId())).thenReturn(cart);
        when(cartItemRepository.findCartItemsByCartId(cart.getId())).thenReturn(List.of(cartItem));
        when(orderRepository.save(any())).thenReturn(new Order());

        OrderResponseDto responseDto = orderService.createOrder(user.getId(), "대전");
        //then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAddress()).isEqualTo("대전");
    }

    @Test
    void 장바구니가_비어있을때_주문생성시_예외가발생한다(){
        //given
        User user = new User("형철@naver.com","123456789!A", UserRole.OWNER);
        Cart cart = Cart.createCart(user, new Store(), LocalDateTime.now().plusHours(1));

        //when
        when(cartRepository.findByUserIdOrElseThrow(user.getId())).thenReturn(cart);

        when(cartItemRepository.findCartItemsByCartId(cart.getId())).thenReturn(Collections.emptyList());

        //then
        assertThatThrownBy(()->orderService.createOrder(user.getId(),"대전")) //해당작업시
                .isInstanceOf(ResponseStatusException.class); //발생 예상되는 예외

    }

    @Test
    void 가게가_마감시간이면_주문생성시_예외가발생한다(){
        //given
        User user = new User("형철@naver.com","123456789!A", UserRole.OWNER);
        Store store = new Store(user,"맛있는 삼겹살", LocalTime.of(9,0),LocalTime.of(10,0),10000L);
        Cart cart = Cart.createCart(user, store, LocalDateTime.now().plusHours(1));


        //when
        when(cartRepository.findByUserIdOrElseThrow(user.getId())).thenReturn(cart);
        when(cartItemRepository.findCartItemsByCartId(cart.getId())).thenReturn(List.of(new CartItem()));

        //then
        assertThatThrownBy(()->orderService.createOrder(user.getId(), "대전"))
                .isInstanceOf(ResponseStatusException.class)    //예상예외
                .hasMessageContaining("가게가 운영중이지 않습니다."); //예상메세지
    }
    @Test
    void 가게가_폐업상태면_주문생성시_예외가발생한다(){
        //given
        User user = new User("형철@naver.com","123456789!A", UserRole.OWNER);
        Store store = new Store(user,"맛있는 삼겹살", LocalTime.of(9,0),LocalTime.of(18,0),10000L);
        Cart cart = Cart.createCart(user, store, LocalDateTime.now().plusHours(1));


        store.isClosedTrue(); //가게폐업

        //when
        when(cartRepository.findByUserIdOrElseThrow(user.getId())).thenReturn(cart);
        when(cartItemRepository.findCartItemsByCartId(cart.getId())).thenReturn(List.of(new CartItem()));

        //then
        assertThatThrownBy(()->orderService.createOrder(user.getId(),"대전"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("가게가 운영중이지 않습니다.");


    }


}

