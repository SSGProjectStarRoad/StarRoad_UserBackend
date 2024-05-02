package com.ssg.starroad.coupon.repository;

import com.ssg.starroad.coupon.entity.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
}
