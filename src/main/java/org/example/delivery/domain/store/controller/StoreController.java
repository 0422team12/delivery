package org.example.delivery.domain.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.store.dto.CreateStoreRequestDto;
import org.example.delivery.domain.store.dto.CreateStoreResponseDto;
import org.example.delivery.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 생성 post
    @PostMapping
    public ResponseEntity<CreateStoreResponseDto> createStore(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateStoreRequestDto requestDto){
        CreateStoreResponseDto responseDto = storeService.createStore(requestDto,token);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED); //201
    }

    // 가게 조회 -> 메뉴 리스트 조회 불가. 가게명에 따른 전체 가게 확인 가능

    // 가게 단일 조회 -> 메뉴 리스트 확인 가능

    // 가게 수정

    // 가게 삭제(폐업)


}
