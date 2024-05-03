package com.ssg.starroad.coupon.repository;

import com.ssg.starroad.coupon.entity.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> , QuerydslPredicateExecutor<CouponHistory>{
}
