package com.ssg.starroad.coupon.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.entity.QCouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponHistoryRepositoryCustom {

    private final CouponHistoryRepository couponHistoryRepository;

    public Optional<CouponHistory> findByCouponId(Long couponID, boolean couponUsageStatus) {
        QCouponHistory qCouponHistory = QCouponHistory.couponHistory;
        BooleanExpression query = qCouponHistory.id.eq(couponID).and(qCouponHistory.usageStatus.eq(false));
        return couponHistoryRepository.findOne(query);
    }
}
