package com.ssg.starroad.coupon.service;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.entity.Coupon;

import java.util.List;

public interface CouponHistoryService {
    public void issueCoupon(Long userID, Long couponID);

    public CouponDTO getCouponById(Long couponID);
    public boolean updateCouponUsage(Long couponID, boolean couponUsageStatus);
    public List<CouponDTO> getCouponsByUserID(Long userID);
}
