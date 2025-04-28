package org.example.delivery.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;

import java.time.LocalTime;

@Entity
@Getter
@Table(name="menus")
@NoArgsConstructor

public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Store store;

    private String name;

    private Long price;

    private String content;

    private boolean isDeleted;

    public Menu(Store store, String name, Long price, String content) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.content = content;
    }

    public void isDeletedTrue() {
        this.isDeleted = true;
    }

    public void update(String name, Long price, String content) {
        this.name = name;
        this.price = price;
        this.content = content;
    }

    public static Menu createMenu(Store store, String name, Long price, String content){
        return new Menu(store, name, price, content);
    }

}
