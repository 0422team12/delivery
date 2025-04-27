package org.example.delivery.domain.cart.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartTest {
    private User user = new User();

    @Mock
    private Store store;

    @Test
    @DisplayName("Cart.createCart는 정상적으로 Cart를 생성해야 한다")
    void testcreateCart() {
        //given
        LocalDateTime expireTime = LocalDateTime.now().plusHours(1);

        //when
        Cart cart = Cart.createCart(user, store, expireTime);

        //then
        assertThat(cart).isNotNull();
        assertThat(cart.getUser()).isEqualTo(user);
        assertThat(cart.getStore()).isEqualTo(store);
        assertThat(cart.getExpiredAt()).isEqualTo(expireTime);
    }

    @Test
    @DisplayName("Cart가 만료되었는지 확인할 수 있다")
    void testisExpired() {
        //given
        Cart expiredCart = Cart.createCart(user, store, LocalDateTime.now().minusMinutes(1));
        Cart validCart = Cart.createCart(user, store, LocalDateTime.now().plusMinutes(1));

        //when, then
        assertThat(expiredCart.isExpired()).isTrue();
        assertThat(validCart.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Cart.updateCartExpiredAt은 만료 시간을 하루 뒤로 갱신한다")
    void testupdateCartExpiredAt() {
        //given
        Cart cart = Cart.createCart(user, store, LocalDateTime.now());
        //when
        cart.updateCartExpiredAt();
        //then 만료 일시가 23시간 후보다 뒤이다.
        assertThat(cart.getExpiredAt()).isAfter(LocalDateTime.now().plusHours(23));
    }

    @Test
    @DisplayName("Cart의 Store ID가 같은지 비교할 수 있다")
    void testisEqualStoreId() {
        when(store.getId()).thenReturn(123L);
        Cart cart = Cart.createCart(user, store, LocalDateTime.now());

        assertThat(cart.isEqualStoreId(123L)).isTrue();
        assertThat(cart.isEqualStoreId(999L)).isFalse();
    }
}