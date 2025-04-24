package org.example.delivery.domain.store.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.store.dto.StoreRequestDto;
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
            HttpServletRequest request,
            @Valid @RequestBody StoreRequestDto requestDto
    ){
        StoreResponseDto responseDto = storeService.createStore(requestDto,request);
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
    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable Long storeId,
            HttpServletRequest request,
            @RequestBody StoreRequestDto requestDto
    ) {
        StoreResponseDto responseDto = storeService.updateStore(storeId, request, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 가게 삭제(폐업)
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> closeStore(
            @PathVariable Long storeId,
            HttpServletRequest request) {
        storeService.closeStore(storeId, request);
        return ResponseEntity.noContent().build(); // 204
    }


}
