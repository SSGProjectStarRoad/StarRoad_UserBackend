package com.ssg.starroad.shop.controller;

import com.ssg.starroad.shop.service.ComplexShoppingmallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complexShoppingmall")
@RequiredArgsConstructor
public class ComplexShoppingmallComtroller {
    private final ComplexShoppingmallService complexShoppingmallService;
}
