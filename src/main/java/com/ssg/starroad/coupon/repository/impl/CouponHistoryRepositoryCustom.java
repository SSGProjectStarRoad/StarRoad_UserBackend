package com.ssg.starroad.coupon.repository.impl;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.starroad.coupon.entity.Coupon;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.entity.QCoupon;
import com.ssg.starroad.coupon.entity.QCouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final CouponHistoryRepository couponHistoryRepository;

    public Optional<CouponHistory> findByCouponId(Long couponID) {
        QCouponHistory qCouponHistory = QCouponHistory.couponHistory;
        BooleanExpression query = qCouponHistory.id.eq(couponID).and(qCouponHistory.usageStatus.eq(false));
        return couponHistoryRepository.findOne(query);
    }

    public List<Coupon> findCouponsByUserId(Long userId) {
        QCouponHistory qCouponHistory = QCouponHistory.couponHistory;
        QCoupon qCoupon = QCoupon.coupon;

        return queryFactory.select(qCoupon)
                .from(qCouponHistory)
                .join(qCoupon).on(qCoupon.id.eq(qCouponHistory.couponId))
                .where(qCouponHistory.user.id.eq(userId))
                .fetch();
    }
}
