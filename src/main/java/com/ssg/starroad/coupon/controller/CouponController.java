package com.ssg.starroad.coupon.controller;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.dto.IssueCouponRequest;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponHistoryService couponHistoryService;

    @PostMapping("/{coupon_id}/issue")
    public ResponseEntity<String> issueCoupon(@RequestBody IssueCouponRequest request) {
        // 로직 구현 부분, 예를 들어 쿠폰 발급 로직
        couponHistoryService.CouponUserAdd(request.getUserId(), request.getCouponId());

        // 성공 응답 반환
        return ResponseEntity.ok("Coupon issued successfully to user ID " + request.getUserId());
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
