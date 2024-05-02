package com.ssg.starroad.coupon.repository;

import com.ssg.starroad.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
