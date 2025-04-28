package org.example.delivery.domain.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.user.entity.User;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="stores")
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private User owner;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private boolean isClosed;

    private Long minOrderValue;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    public Store(User owner, String name, LocalTime openingTime, LocalTime closingTime, Long minOrderValue) {
        this.owner = owner;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minOrderValue = minOrderValue;
    }

    public void update(String name, LocalTime openingTime, LocalTime closingTime, Long minOrderValue){
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minOrderValue = minOrderValue;
    }

    public void isClosedTrue() {
        this.isClosed = true;
    }

    public boolean isOverMinOrderValue(Long totalPrice){
        return totalPrice >= this.minOrderValue;
    }

    public static Store createStore(User owner, String name, LocalTime openingTime, LocalTime closingTime, Long minOrderValue) {
        return new Store(owner, name, openingTime, closingTime, minOrderValue);
    }

}