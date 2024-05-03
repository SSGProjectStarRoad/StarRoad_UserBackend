package com.ssg.starroad.coupon.service.impl;

import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.entity.Coupon;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import com.ssg.starroad.coupon.repository.CouponRepository;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    @Override
    public void issueCoupon(Long userID, Long couponID) {
        CouponHistory couponHistory =
                new CouponHistory(null, userRepository.findById(userID).orElseThrow(),
                        couponID,false, LocalDate.now().plusDays(7));
        couponHistoryRepository.save(couponHistory);
    }

    @Override
    public CouponDTO getCouponById(Long couponID) {
        return   modelMapper.map(couponRepository.findById(couponID),CouponDTO.class);
    }
}
