package org.example.delivery.domain.menu.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.CreateMenuRequestDto;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long storeId,
            @RequestBody CreateMenuRequestDto requestDto,
            HttpServletRequest request
    ) {
        MenuResponseDto responseDto = menuService.createMenu(storeId, requestDto, request);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 메뉴 수정

    // 메뉴 삭제

    // + 메뉴 조회는 가게 조회시 함께 조회

}
