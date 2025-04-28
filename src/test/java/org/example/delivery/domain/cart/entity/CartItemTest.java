package org.example.delivery.domain.cart.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.delivery.domain.menu.entity.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemTest {

    private final Cart cart = new Cart();
    @Mock
    private Menu menu;

    @Test
    @DisplayName("CartItem.createCartItem은 정상적으로 CartItem를 생성해야 한다")
    void testCreateCartItem() {
        //given
        when(menu.getPrice()).thenReturn(5000L);
        //when
        CartItem cartItem = CartItem.createCartItem(cart, menu, 5);

        //then
        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getCart()).isEqualTo(cart);
        assertThat(cartItem.getMenu()).isEqualTo(menu);
        assertThat(cartItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("CartItem의 수량을 수정할 수 있다.")
    void testUpdateQuantity() {
        //given
        when(menu.getPrice()).thenReturn(5000L);
        CartItem cartItem = CartItem.createCartItem(cart, menu, 5);
        //when
        cartItem.updateQuantity(10);
        //then
        assertThat(cartItem.getQuantity()).isEqualTo(10);
    }
}