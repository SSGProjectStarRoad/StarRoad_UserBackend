package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.DTO.CouponDTO;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon-history")
public class CouponHistoryController {

    private final CouponHistoryService couponHistoryService;

    @GetMapping("/{coupon_history_id}")
    public ResponseEntity<CouponDTO> getCoupon(@PathVariable("coupon_history_id") Long couponId) {
        CouponDTO coupon=couponHistoryService.CouponDetails(couponId);
        if (coupon.getCouponId()==0) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(coupon);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }

    @GetMapping("/{coupon_history_id}/use")
    public ResponseEntity<?> updateCouponUsage(@PathVariable("coupon_history_id") Long couponHistoryId) {
        boolean updated = couponHistoryService.CouponUsageModify(couponHistoryId);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
