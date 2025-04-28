package org.example.delivery.domain.ad.controller;


import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.ad.dto.ApplyAdRequestDto;
import org.example.delivery.domain.ad.dto.FindAdResponseDto;
import org.example.delivery.domain.ad.service.AdService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stores/{storeId}/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;


    @PostMapping()
    public ResponseEntity<Long> applyAd(
            @RequestAttribute Long userId,
            @PathVariable Long storeId,
            @RequestBody ApplyAdRequestDto dto
            ){
        Long adId = adService.applyAd(userId, storeId, dto);

        return ResponseEntity.ok(adId);
    }

    @GetMapping("/{adId}")
    public ResponseEntity<FindAdResponseDto> findAd(
            @RequestAttribute Long userId,
            @PathVariable Long storeId,
            @PathVariable Long adId
    ){
        FindAdResponseDto resDto = adService.findOwnedAd(userId, storeId, adId);

        return ResponseEntity.ok(resDto);
    }

}
