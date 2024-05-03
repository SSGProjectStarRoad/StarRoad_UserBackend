package com.ssg.starroad.coupon.repository;

import com.ssg.starroad.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CouponRepository extends JpaRepository<Coupon, Long>, QuerydslPredicateExecutor<Coupon>{
}
