package org.example.delivery.domain.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.user.entity.User;

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

    @ManyToOne
    private User owner;

    private String name;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private boolean isClosed;

    private Long minOrderValue;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    public Store(User owner, String name, LocalTime openingTime, LocalTime closingTime, boolean isClosed, Long minOrderValue) {
        this.owner = owner;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.isClosed = isClosed;
        this.minOrderValue = minOrderValue;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void updateClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public void updateMinOrderValue(Long minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public void isClosedTrue() {
        this.isClosed = true;
    }


}



