package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.reward.DTO.RewardDTO;
import com.ssg.starroad.shop.DTO.StoreDTO;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public List<StoreDTO> searchStoreList(Long id) {
        List<Store> stores = storeRepository.findByComplexShoppingmallId(id).orElseThrow(() -> new RuntimeException("dkdjlka"));

        List<StoreDTO> storeDTOList = stores.stream()
                .map(Store::toDTO).collect(Collectors.toList());

        return storeDTOList;
    }

    }


