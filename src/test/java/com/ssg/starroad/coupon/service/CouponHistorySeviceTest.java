package com.ssg.starroad.coupon.service;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import com.ssg.starroad.coupon.repository.CouponRepository;
import com.ssg.starroad.coupon.repository.CouponRepositoryTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CouponHistorySeviceTest {

    private static final Logger log = LogManager.getLogger(CouponRepositoryTest.class);
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponHistoryService couponHistoryService;

    @Autowired
    private CouponHistoryRepository couponHistoryRepository;

    @Test
    public void testDetailCoupon(){
        CouponDTO couponDTO =couponHistoryService.getCouponById(1L);
        log.info("coupon detail :{}", couponDTO.toString());
    }

    @Test
    public void testupdateCouponUsage(){
        couponHistoryService.updateCouponUsage(1L,false);
        log.info("coupon status: {} ",couponHistoryRepository.findById(1L));
    }

}
