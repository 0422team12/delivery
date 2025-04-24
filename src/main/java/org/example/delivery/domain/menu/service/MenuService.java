package org.example.delivery.domain.menu.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.CreateMenuRequestDto;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.entity.Menu;
import org.example.delivery.domain.menu.repository.MenuRepository;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.repository.StoreRepository;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 메뉴 생성 -> 로그인한 사장님만 가능, 가게 id에 할당해줘야함
    public MenuResponseDto createMenu(Long storeId, CreateMenuRequestDto requestDto, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        Menu menu = new Menu(
                store,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getContent(),
                false);

        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponseDto(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getContent());


    }

}
