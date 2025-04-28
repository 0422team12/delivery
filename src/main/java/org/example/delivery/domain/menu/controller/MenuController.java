package org.example.delivery.domain.menu.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.menu.dto.MenuCreateRequestDto;
import org.example.delivery.domain.menu.dto.MenuResponseDto;
import org.example.delivery.domain.menu.dto.MenuUpdateRequestDto;
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
            @Valid @RequestBody MenuCreateRequestDto requestDto,
            HttpServletRequest request
    ) {
        MenuResponseDto responseDto = menuService.createMenu(
                storeId,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getContent(),
                request);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 메뉴 수정
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        MenuResponseDto responseDto = menuService.updateMenu(
                menuId,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getContent(),
                request);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteMenu(
            @PathVariable Long menuId,
            HttpServletRequest request
    ) {
        menuService.deleteMenu(menuId, request);
        return new ResponseEntity<>("삭제 되었습니다.", HttpStatus.NO_CONTENT);
    }

}
