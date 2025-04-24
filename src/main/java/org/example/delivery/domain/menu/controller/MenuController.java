package org.example.delivery.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.service.MenuService;
import org.example.delivery.domain.store.dto.CreateStoreRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody CreateStoreRequestDto requestDto){

    }

    // 메뉴 수정

    // 메뉴 삭제

    // + 메뉴 조회는 가게 조회시 함께 조회

}
