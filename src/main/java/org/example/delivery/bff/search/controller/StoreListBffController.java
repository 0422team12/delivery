package org.example.delivery.bff.search.controller;


import lombok.RequiredArgsConstructor;
import org.example.delivery.bff.search.dto.StoreListResponseDto;
import org.example.delivery.domain.ad.dto.FindAdResponseDto;
import org.example.delivery.domain.ad.service.AdService;
import org.example.delivery.domain.store.dto.StoreResponseDto;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bff")
public class StoreListBffController {

    private final StoreService storeService;
    private final AdService adService;

    @GetMapping("/store-list")
    public ResponseEntity<List<StoreListResponseDto>> showStoreListBff(
            @RequestParam String name
    ) {
        // storeService + adsService

        List<StoreResponseDto> storeList = storeService.getStoresByName(name);

        List<StoreListResponseDto> res = new ArrayList<>();

        if (storeList.isEmpty()) return ResponseEntity.ok(res);


        // storeId로 ads 조회. 존재 시 상위에 추가 (중복하여 표시)
        List<Long> storeIds = storeList.stream()
                .map(StoreResponseDto::getId)
                .toList();

        List<StoreListResponseDto> ads = adService.findAds(storeIds);
        res.addAll(ads);

        // Dto 변환
        res.addAll(storeList.stream()
                .map(store -> new StoreListResponseDto(
                        store.getId(),
                        store.getName(),
                        store.getOpeningTime(),
                        store.getClosingTime(),
                        store.getMinOrderValue()
                )).toList()
        );


        return ResponseEntity.ok(res);

    }

}
