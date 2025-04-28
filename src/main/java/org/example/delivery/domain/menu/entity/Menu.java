package org.example.delivery.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.store.entity.Store;

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

    public Menu(Store store, String name, Long price, String content, boolean isDeleted) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void isDeletedTrue() {
        this.isDeleted = true;
    }

}
