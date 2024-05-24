package com.ssg.starroad.coupon.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.starroad.coupon.DTO.CouponDTO;
import com.ssg.starroad.coupon.entity.CouponHistory;
import com.ssg.starroad.coupon.entity.QCoupon;
import com.ssg.starroad.coupon.entity.QCouponHistory;
import com.ssg.starroad.coupon.repository.CouponHistoryRepository;
import lombok.RequiredArgsConstructor;
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

    public List<CouponDTO> findCouponsByUserId(Long userId) {
        QCoupon qCoupon = QCoupon.coupon;
        QCouponHistory qCouponHistory = QCouponHistory.couponHistory;

        List<CouponDTO> coupons = queryFactory
                .select(Projections.fields(CouponDTO.class,
                        qCouponHistory.id.as("couponHistoryId"),
                        qCoupon.id.as("couponId"),
                        qCoupon.name.as("couponName"),
                        qCouponHistory.discountRate.as("couponDiscountRate"),
                        qCoupon.discountAmount.as("couponDiscountAmount"),
                        qCoupon.status.as("couponStatus"),
                        qCouponHistory.usageStatus.as("couponUsageStatus"),
                        qCouponHistory.expiredAt.as("couponExpiredAt"),
                        qCoupon.shopType.as("couponShopType"),
                        qCoupon.minAmount.as("couponMinAmount"),
                        qCoupon.maxAmount.as("couponMaxAmount")))
                .from(qCouponHistory)
                .join(qCoupon).on(qCoupon.id.eq(qCouponHistory.couponId))
                .where(qCouponHistory.user.id.eq(userId))
                .fetch();

        System.out.println("Coupons fetched: " + coupons); // 로그 추가
        return coupons;
    }
}
