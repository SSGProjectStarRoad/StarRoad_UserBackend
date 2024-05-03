package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.dto.CouponUsageDTO;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{couponHistoryId}/use")
    public ResponseEntity<?> updateCouponUsage(@PathVariable("couponHistoryId") Long couponId, @RequestBody CouponUsageDTO couponUsageDTO) {
        boolean updated = couponHistoryService.updateCouponUsage(couponId, couponUsageDTO.isCouponUsageStatus());
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
