package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.DTO.CouponDTO;
import com.ssg.starroad.coupon.DTO.IssueCouponRequest;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponHistoryService couponHistoryService;

    @PostMapping("/issue")
    public ResponseEntity<Integer> issueCoupon(@RequestBody IssueCouponRequest request) {

        if (request.getUserId() == null || request.getCouponId() == null) {
            return ResponseEntity.badRequest().body(0);
        }

        int rate = couponHistoryService.CouponUserAdd(request.getUserId(), request.getCouponId());

        return ResponseEntity.ok(rate);
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
