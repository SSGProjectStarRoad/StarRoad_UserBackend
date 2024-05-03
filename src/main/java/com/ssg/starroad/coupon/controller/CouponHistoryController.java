package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon-history")
public class CouponHistoryController {

    @Autowired
    private CouponHistoryService couponHistoryService;

    @GetMapping("/{coupon_history_id}")
    public CouponDTO getCoupon(@PathVariable("coupon_history_id") Long couponId) {
        return couponHistoryService.getCouponById(couponId);
    }
}
