package com.ssg.starroad.coupon.service;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.entity.Coupon;

import java.util.List;

public interface CouponHistoryService {


    void CouponUserAdd(Long userID, Long couponID);



    CouponDTO CouponDetails(Long couponID);



    boolean CouponUsageModify(Long couponID);



    List<CouponDTO> CouponsUserList(Long userID);
}
