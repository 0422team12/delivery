package org.example.delivery.domain.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.user.entity.User;
import org.springframework.lang.Contract;

import java.time.LocalTime;

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

    private LocalTime closing_time;

    private boolean isClosed;

    private Long minOrderValue;

    public Store(User owner, String name, LocalTime openingTime, LocalTime closing_time, boolean isClosed, Long minOrderValue) {
        this.owner = owner;
        this.name = name;
        this.openingTime = openingTime;
        this.closing_time = closing_time;
        this.isClosed = isClosed;
        this.minOrderValue = minOrderValue;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void updateClosing_time(LocalTime closing_time) {
        this.closing_time = closing_time;
    }

    public void updateMinOrderValue(Long minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public void isClosedTrue() {
        this.isClosed = true;
    }


}



