package org.example.delivery.domain.store.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.store.dto.StoreCreateRequestDto;
import org.example.delivery.domain.store.dto.StoreDetailResponseDto;
import org.example.delivery.domain.store.dto.StoreResponseDto;
import org.example.delivery.domain.store.dto.StoreUpdateRequestDto;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.repository.StoreRepository;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 가게 생성 서비스 => 사장만 생성 가능, 인당 최대 3가게까지 생성 가능
    public StoreResponseDto createStore(StoreCreateRequestDto requestDto, Long userId, UserRole userRole) {

        if (userRole != UserRole.OWNER) {
            throw new IllegalArgumentException("접근 권한이 없습니다."); // 사장만 권한 있음
        }

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(storeRepository.countByOwnerAndIsClosedFalse(owner) >= 3) {
            throw new IllegalArgumentException("가게는 최대 3개까지만 생성할 수 있습니다.");
        }

        Store store = Store.createStore(
                owner,
                requestDto.getName(),
                requestDto.getOpeningTime(),
                requestDto.getClosingTime(),
                requestDto.getMinOrderValue()
        );

        Store saved = storeRepository.save(store);

        return StoreResponseDto.of(saved);

    }

    // 가게 조회
    public List<StoreResponseDto> getStoresByName(String name) {

        List<Store> storeList = storeRepository.findAllByNameContainingAndIsClosedFalse(name); // 폐업하지 않은 가게들만 조회

        return storeList.stream()
                .map(StoreResponseDto::of)
                .toList();
    }

    // 가게 단건 조회
    public StoreDetailResponseDto getStoreById(Long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다.")); // 추후 예외처리 repository로 빼서 관리

        if (store.isClosed()) { //isClosed가 ture면 -> 폐업한 가게. 조회불가
            throw new IllegalStateException("가게를 찾을 수 없습니다.");
        }

        List<Menu> activeMenus = store.getMenuList().stream() // 삭제되지 않은 메뉴만 필터링
                .filter(menu -> !menu.isDeleted())
                .collect(Collectors.toList());

        // Menu -> MenuResponseDto 변환
        List<MenuResponseDto> menuResponseDtoList = activeMenus.stream()
                .map(MenuResponseDto::of)
                .collect(Collectors.toList());

        return  StoreDetailResponseDto.of(store, menuResponseDtoList);

    }

    // 가게 수정
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto requestDto, Long userId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다."); // 사장만 접근 가능
        }

        store.update(requestDto.getName(), requestDto.getOpeningTime(), requestDto.getClosingTime(), requestDto.getMinOrderValue());

        Store updated = storeRepository.save(store);

        return StoreResponseDto.of(updated);
    }

    // 가게 삭제
    @Transactional
    public void closeStore(Long storeId, Long userId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        store.isClosedTrue(); // 폐업 처리

        storeRepository.save(store);
    }


}
