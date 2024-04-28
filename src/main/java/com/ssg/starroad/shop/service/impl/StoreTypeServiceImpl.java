package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.shop.repository.StoreTypeRepository;
import com.ssg.starroad.shop.service.StoreTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreTypeServiceImpl implements StoreTypeService {
    private final StoreTypeRepository storeTypeRepository;
}
