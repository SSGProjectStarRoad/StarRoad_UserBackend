package com.ssg.starroad.shop.service.impl;

import com.ssg.starroad.shop.entity.ComplexShoppingmall;
import com.ssg.starroad.shop.repository.ComplexShoppingmallRepository;
import com.ssg.starroad.shop.service.ComplexShoppingmallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplexShoppingmallServiceImpl implements ComplexShoppingmallService {
    private final ComplexShoppingmallRepository complexShoppingmallRepository;
}
