package com.ssg.starroad.shop.controller;

import com.ssg.starroad.shop.service.StoreTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/store")
@RestController
@RequiredArgsConstructor
public class StoreTypeController {
    private final StoreTypeService storeTypeService;
}
