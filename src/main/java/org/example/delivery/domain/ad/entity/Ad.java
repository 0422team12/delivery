package org.example.delivery.domain.ad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.domain.store.entity.Store;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private Integer priority;

    private boolean isActive;

    public Ad(Store store, LocalDateTime startAt, LocalDateTime endAt, Integer priority, boolean isActive){
        this.store = store;
        this.startAt = startAt;
        this.endAt = endAt;
        this.priority = priority;
        this.isActive = isActive;
    }

}
