package org.example.delivery.domain.cart.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.cart.entity.Cart;
import org.example.delivery.domain.cart.repository.CartRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartCleanupScheduler {

    private final CartRepository cartRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30) //30분마다
    @Transactional
    public void deleteExpiredCarts() {
        //만료시간이 현재보다 이전인 경우를 조회해온다.
        List<Cart> expiredCarts = cartRepository.findAllByExpiredAtBefore(LocalDateTime.now());
        cartRepository.deleteAll(expiredCarts);
    }

    @PostConstruct
    public void onStartupCleanup() {
        deleteExpiredCarts(); // 앱 시작 시 정리
    }

    @PreDestroy
    public void onShutdownCleanup() {
        deleteExpiredCarts(); // 앱 종료 시 정리
    }
}
