package org.example.delivery.domain.cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "carts")
public class Cart { //장바구니
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true) //유저는 1개의 장바구니만 사용 가능
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private LocalDateTime expiredAt;             //장바구니 만료 시간, 수동 업데이트

    private Cart (User user, Store store){
        this.user = user;
        this.store = store;
    }

    public static Cart createCart(User user, Store store){
        return new Cart(user, store);
    }

    public void updateCartExpriedAt(){
        this.expiredAt = LocalDateTime.now().plusDays(1);
    }

    public Boolean isEqualStoreId(Long storeId){
        return this.store.getId().equals(storeId); //추후 store entity의 메서드에 따라 변경 가능성
    }

}