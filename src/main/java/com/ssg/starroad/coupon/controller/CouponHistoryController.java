package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.dto.CouponUsageDTO;
import com.ssg.starroad.coupon.entity.Coupon;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon-history")
public class CouponHistoryController {

    @Autowired
    private CouponHistoryService couponHistoryService;

    @GetMapping("/{coupon_history_id}")
    public ResponseEntity<CouponDTO> getCoupon(@PathVariable("coupon_history_id") Long couponId) {
        CouponDTO coupon=couponHistoryService.CouponDetails(couponId);
        if (coupon.getCouponId()==0) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(coupon);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }

    @PatchMapping("/{couponHistoryId}/use")
    public ResponseEntity<?> updateCouponUsage(@PathVariable("couponHistoryId") Long couponId, @RequestBody CouponUsageDTO couponUsageDTO) {
        boolean updated = couponHistoryService.CouponUsageModify(couponId, couponUsageDTO.isCouponUsageStatus());
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/coupon/list")
    public ResponseEntity<List<CouponDTO>> getUserCoupons(@PathVariable Long userId) {
        List<CouponDTO> coupons = couponHistoryService.CouponsUserList(userId);
        if (coupons.isEmpty()) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(coupons);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }
}
