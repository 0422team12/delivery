package org.example.delivery.domain.store.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.delivery.config.JwtUtil;
import org.example.delivery.domain.store.dto.CreateStoreRequestDto;
import org.example.delivery.domain.store.dto.StoreDetailResponseDto;
import org.example.delivery.domain.store.dto.StoreResponseDto;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.repository.StoreRepository;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 가게 생성 서비스 => 사장만 생성 가능, 인당 최대 3가게까지 생성 가능
    public StoreResponseDto createStore(CreateStoreRequestDto requestDto, String token) {

        UserRole userRole = jwtUtil.getClaims(token).get("userRole", UserRole.class); // 토큰에서 유저 롤 추출

        if (userRole != UserRole.OWNER) {
            throw new IllegalArgumentException("접근 권한이 없습니다."); // 사장만 권한 있음
        }

        // JWT에서 사용자 ID 추출
        Long userId = jwtUtil.getUserIdFromToken(token);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(storeRepository.countByOwner(owner) >= 3){
            throw new IllegalArgumentException("가게는 최대 3개까지만 생성할 수 있습니다.");
        }

        Store store = new Store(
                owner,
                requestDto.getName(),
                requestDto.getOpeningTime(),
                requestDto.getClosing_time(),
                false,
                requestDto.getMinOrderValue()
        ); // 저장되어야 할 정보 : request로 받아온 가게이름,오픈시간,클로징시간,최소주문금액 & 사장님 이름(?? 어떻게 저장)

        Store saved = storeRepository.save(store);

        return new StoreResponseDto(saved.getName(), saved.getOpeningTime(), saved.getClosing_time(), saved.getMinOrderValue());

    }

    // 가게 조회
    public List<StoreResponseDto> getStoresByName(String name) {

        List<Store> storeList = storeRepository.findAllByNameContainingAndIsClosedFalse(name); // 폐업하지 않은 가게들만 조회

        return storeList.stream()
                .map(store -> new StoreResponseDto(
                        store.getName(),
                        store.getOpeningTime(),
                        store.getClosing_time(),
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

        return new StoreDetailResponseDto(store.getName(), store.getOpeningTime(), store.getClosing_time(), store.getMinOrderValue());

    }

    // 가게 수정
    @Transactional
    public StoreResponseDto updateStore(Long storeId, String token, CreateStoreRequestDto requestDto) {
        Long userId = jwtUtil.getUserIdFromToken(token);

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

        if (requestDto.getClosing_time() != null) {
            store.updateClosing_time(requestDto.getClosing_time());
        }

        if (requestDto.getMinOrderValue() != null) {
            store.updateMinOrderValue(requestDto.getMinOrderValue());
        }

        Store updated = storeRepository.save(store);

        return new StoreResponseDto(updated.getName(), updated.getOpeningTime(), updated.getClosing_time(), updated.getMinOrderValue());
    }

    // 가게 삭제
    @Transactional
    public void closeStore(Long storeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        store.isClosedTrue(); // 폐업 처리

        storeRepository.save(store);
    }


}
