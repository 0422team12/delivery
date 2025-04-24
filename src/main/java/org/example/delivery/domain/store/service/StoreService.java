package org.example.delivery.domain.store.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.store.dto.StoreRequestDto;
import org.example.delivery.domain.store.dto.StoreDetailResponseDto;
import org.example.delivery.domain.store.dto.StoreResponseDto;
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
    public StoreResponseDto createStore(StoreRequestDto requestDto, HttpServletRequest request) {

        UserRole userRole = UserRole.valueOf((String) request.getAttribute("userRole")); // userroll 추출

        if (userRole != UserRole.OWNER) {
            throw new IllegalArgumentException("접근 권한이 없습니다."); // 사장만 권한 있음
        }

        Long userId = (Long) request.getAttribute("userId");
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(storeRepository.countByOwner(owner) >= 3){
            throw new IllegalArgumentException("가게는 최대 3개까지만 생성할 수 있습니다.");
        }

        Store store = new Store(
                owner,
                requestDto.getName(),
                requestDto.getOpeningTime(),
                requestDto.getClosingTime(),
                false,
                requestDto.getMinOrderValue()
        ); // 저장되어야 할 정보 : request로 받아온 가게이름,오픈시간,클로징시간,최소주문금액 & 사장님 이름(?? 어떻게 저장)

        Store saved = storeRepository.save(store);

        return new StoreResponseDto(saved.getId(), saved.getName(), saved.getOpeningTime(), saved.getClosingTime(), saved.getMinOrderValue());

    }

    // 가게 조회
    public List<StoreResponseDto> getStoresByName(String name) {

        List<Store> storeList = storeRepository.findAllByNameContainingAndIsClosedFalse(name); // 폐업하지 않은 가게들만 조회

        return storeList.stream()
                .map(store -> new StoreResponseDto(
                        store.getId(),
                        store.getName(),
                        store.getOpeningTime(),
                        store.getClosingTime(),
                        store.getMinOrderValue()
                )).toList();

    }

    // 가게 단건 조회
    // todo : return시 메뉴리스트 추가해야함
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
                .map(menu -> new MenuResponseDto(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.getContent()))
                .collect(Collectors.toList());

        return new StoreDetailResponseDto(store.getId(), store.getName(), store.getOpeningTime(), store.getClosingTime(), store.getMinOrderValue(), menuResponseDtoList);

    }

    // 가게 수정
    @Transactional
    public StoreResponseDto updateStore(Long storeId, HttpServletRequest request, StoreRequestDto requestDto) {

        Long userId = (Long) request.getAttribute("userId");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다."); // 사장만 접근 가능
        }

        if (requestDto.getName() != null) {
            store.updateName(requestDto.getName());
        }

        if (requestDto.getOpeningTime() != null) {
            store.updateOpeningTime(requestDto.getOpeningTime());
        }

        if (requestDto.getClosingTime() != null) {
            store.updateClosingTime(requestDto.getClosingTime());
        }

        if (requestDto.getMinOrderValue() != null) {
            store.updateMinOrderValue(requestDto.getMinOrderValue());
        }

        Store updated = storeRepository.save(store);

        return new StoreResponseDto(updated.getId(), updated.getName(), updated.getOpeningTime(), updated.getClosingTime(), updated.getMinOrderValue());
    }

    // 가게 삭제
    @Transactional
    public void closeStore(Long storeId, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        store.isClosedTrue(); // 폐업 처리

        storeRepository.save(store);
    }


}
