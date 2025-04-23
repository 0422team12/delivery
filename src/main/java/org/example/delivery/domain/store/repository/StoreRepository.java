package org.example.delivery.domain.store.repository;

import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    int countByOwner(User owner);

    List<Store> findAllByNameContainingAndClosedIsFalse(String name);

}
