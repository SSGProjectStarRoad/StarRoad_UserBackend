package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.shop.repository.StoreRepository;
import com.ssg.starroad.shop.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
}
