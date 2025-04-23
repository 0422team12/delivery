package org.example.delivery.domain.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.store.dto.CreateStoreRequestDto;
import org.example.delivery.domain.store.dto.StoreDetailResponseDto;
import org.example.delivery.domain.store.dto.StoreResponseDto;
import org.example.delivery.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 생성 post
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateStoreRequestDto requestDto
    ){
        StoreResponseDto responseDto = storeService.createStore(requestDto,token);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED); //201
    }

    // 가게 조회 -> 메뉴는 조회 불가. 가게명에 따른 전체 가게 확인 가능
    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getStoresByName(
            @RequestParam("name") String name
    ){
        List<StoreResponseDto> responseDtoList = storeService.getStoresByName(name);
        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }

    // 가게 단일 조회 -> 메뉴 리스트 확인 가능
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailResponseDto> getStoreById(@PathVariable Long storeId) {
        StoreDetailResponseDto responseDto = storeService.getStoreById(storeId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 가게 수정

    // 가게 삭제(폐업)


}
