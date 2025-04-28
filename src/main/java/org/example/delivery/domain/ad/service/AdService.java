package org.example.delivery.domain.ad.service;


import lombok.RequiredArgsConstructor;
import org.example.delivery.bff.search.dto.StoreListResponseDto;
import org.example.delivery.domain.ad.dto.ApplyAdRequestDto;
import org.example.delivery.domain.ad.dto.FindAdResponseDto;
import org.example.delivery.domain.ad.entity.Ad;
import org.example.delivery.domain.ad.exception.AdNotFoundException;
import org.example.delivery.domain.ad.exception.OwnerMismatchException;
import org.example.delivery.domain.ad.exception.StoreNotFoundException;
import org.example.delivery.domain.ad.repository.AdRepository;
import org.example.delivery.domain.store.entity.Store;
import org.example.delivery.domain.store.repository.StoreRepository;
import org.example.delivery.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    @Transactional
    public Long applyAd(Long userId, Long storeId, ApplyAdRequestDto dto) {
        Store store = validOwner(userId, storeId);

        if (!userId.equals(store.getOwner().getId()))
            throw new OwnerMismatchException();

        Ad ad = new Ad(store, dto.getStartAt(), dto.getEndAt(), dto.getPriority(), dto.isActive());

        adRepository.save(ad);

        return ad.getId();
    }

    @Transactional
    public FindAdResponseDto findOwnedAd(Long userId, Long storeId, Long adId) {
        validOwner(userId, storeId);

        Ad ad = adRepository.findById(adId).orElseThrow(AdNotFoundException::new);

        return new FindAdResponseDto(adId, storeId, ad.getStartAt(), ad.getEndAt(), ad.getPriority(), ad.isActive());

    }

    @Transactional
    public List<StoreListResponseDto> findAds(List<Long> storeIds){
        List<Ad> ads = adRepository.findAdByStoreIdIn(storeIds);

        LocalDateTime now = LocalDateTime.now();

        ads.removeIf(ad ->
                (!ad.isActive() || ad.getStartAt().isBefore(now) || ad.getEndAt().isAfter(now)));

        return ads.stream()
                .sorted(Comparator.comparing(Ad::getPriority))
                .map(Ad::getStore)
                .map(ad -> new StoreListResponseDto(
                        ad.getId(),
                        ad.getName(),
                        ad.getOpeningTime(),
                        ad.getClosingTime(),
                        ad.getMinOrderValue()
                )).toList();
    }


    @Transactional(readOnly = true)
    public Store validOwner(Long userId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        if (!userId.equals(store.getOwner().getId()))
            throw new OwnerMismatchException();

        return store;
    }


}
