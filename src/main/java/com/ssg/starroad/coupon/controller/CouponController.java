package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.dto.IssueCouponRequest;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponHistoryService couponHistoryService;

    @PostMapping("/{coupon_id}/issue")
    public ResponseEntity<String> issueCoupon(@RequestBody IssueCouponRequest request) {
        // 로직 구현 부분, 예를 들어 쿠폰 발급 로직
        couponHistoryService.issueCoupon(request.getUserId(), request.getCouponId());

        // 성공 응답 반환
        return ResponseEntity.ok("Coupon issued successfully to user ID " + request.getUserId());
    }

}
