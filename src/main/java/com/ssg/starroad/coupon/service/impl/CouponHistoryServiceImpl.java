package com.ssg.starroad.coupon.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ssg.starroad.coupon.dto.CouponDTO;
import com.ssg.starroad.coupon.entity.Coupon;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.entity.QCouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import com.ssg.starroad.coupon.repository.CouponRepository;
import com.ssg.starroad.coupon.repository.impl.CouponHistoryRepositoryCustom;
import com.ssg.starroad.coupon.service.CouponHistoryService;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final CouponHistoryRepositoryCustom couponHistoryRepositoryCustom;

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

    @Override
    public boolean updateCouponUsage(Long couponID, boolean couponUsageStatus) {

        // 쿠폰 조회
        Optional<CouponHistory> optionalCouponHistory = couponHistoryRepositoryCustom.findByCouponId(couponID,couponUsageStatus);
        if (optionalCouponHistory.isPresent() && !couponUsageStatus) {
            CouponHistory couponHistory = optionalCouponHistory.get();
            couponHistory.useCoupon(); // 상태를 true로 변경
            couponHistoryRepository.save(couponHistory); // 변경 사항 저장
            return true; // 성공적으로 업데이트 되었다면 true 반환
        }
        return false; // 쿠폰을 찾지 못했거나 이미 사용 중이라면 false 반환
    }
}
