package org.example.delivery.domain.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.user.entity.User;

import java.time.LocalTime;

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

    private LocalTime closing_time;

    private boolean isClosed;

    private Long minOrderValue;

}
