package org.example.delivery.domain.store.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.example.delivery.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    @Test
    @DisplayName("총 금액이 최소 주문 금액을 넘기면 true를 반환한다.")
    void testIsOverMinOrderValueReturnTrue(){
        //given
        Long minOrderValue = 10000L;
        Store store = Store.createStore(
                new User(),
                "testStore",
                LocalTime.of(10, 0),
                LocalTime.of(19 , 0),
                minOrderValue
        );

        Long totalPrice = 15000L;

        //when
        //then
        assertThat(store.isOverMinOrderValue(totalPrice)).isEqualTo(true);
    }

    @Test
    @DisplayName("총 금액이 최소 주문 금액과 동일하면 true를 반환한다.")
    void testIsEqualMinOrderValueReturnTrue(){
        //given
        Long minOrderValue = 10000L;
        Store store = Store.createStore(
                new User(),
                "testStore",
                LocalTime.of(10, 0),
                LocalTime.of(19 , 0),
                minOrderValue
        );


        //when
        //then
        assertThat(store.isOverMinOrderValue(minOrderValue)).isEqualTo(true);
    }

    @Test
    @DisplayName("총 금액이 최소 주문 금액 미만이면 false를 반환한다.")
    void testIsNonOverMinOrderValueReturnFalse(){
        //given
        Long minOrderValue = 10000L;
        Store store = Store.createStore(
                new User(),
                "testStore",
                LocalTime.of(10, 0),
                LocalTime.of(19 , 0),
                minOrderValue
        );

        Long totalPrice = 9999L;

        //when
        //then
        assertThat(store.isOverMinOrderValue(totalPrice)).isEqualTo(false);
    }
}