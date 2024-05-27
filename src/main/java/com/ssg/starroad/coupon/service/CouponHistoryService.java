package com.ssg.starroad.coupon.service;

import com.ssg.starroad.coupon.DTO.CouponDTO;

import java.util.List;

public interface CouponHistoryService {


    int CouponUserAdd(String userEmail, Long couponID);



    CouponDTO CouponDetails(Long couponID);



    boolean CouponUsageModify(Long couponID);



    List<CouponDTO> CouponsUserList(String email);
}
