package org.example.delivery.domain.menu.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.MenuCreateRequestDto;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.menu.repository.MenuRepository;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 메뉴 생성 -> 로그인한 사장님만 가능, 가게 id에 할당해줘야함
    public MenuResponseDto createMenu(Long storeId, String name, Long price, String content, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        Menu menu = new Menu(
                store,
                name,
                price,
                content
                );

        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponseDto(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getContent());

    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(Long menuId, String name, Long price, String content, HttpServletRequest request) {
        // 1. 메뉴 id 받아와서 null 체크 후 정보 있으면 menu 가져옴
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        // 2. request 체크해서 로그인한 사장님만 수정 가능
        Long userId = (Long) request.getAttribute("userId"); // 로그인한 user id 가져옴
        if (!menu.getStore().getOwner().getId().equals(userId)){ // 메뉴랑 매핑된 가게의 주인의 아이디 가져와서 로그인 아디랑 비교
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        // 3. 메뉴 리퀘스트 받아온걸로 수정
        menu.update(name, price, content);

        Menu updated = menuRepository.save(menu);

        return new MenuResponseDto(updated.getId(), updated.getName(), updated.getPrice(), updated.getContent());

    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long menuId, HttpServletRequest request) {
        // 본인 가게만 삭제 가능
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        Long userId = (Long) request.getAttribute("userId"); // 로그인한 user id 가져옴
        if (!menu.getStore().getOwner().getId().equals(userId)){ // 메뉴랑 매핑된 가게의 주인의 아이디 가져와서 로그인 아디랑 비교
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        // 삭제시 메뉴 상태만 삭제 상태로 변경 => 소프트 딜리트로 isDeleted = true로 변경해서 저장, true면 조회 불가
        menu.isDeletedTrue();

        // 가게 메뉴 조회시 삭제된 메뉴 나타나지 않음

    }

}

