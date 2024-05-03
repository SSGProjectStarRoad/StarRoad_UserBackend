package com.ssg.starroad.coupon.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private int couponId;
    private String couponName;
    private int couponDiscountRate;
    private int couponDiscountAmount;
    private String couponStatus;
    private boolean couponUsageStatus;
    private LocalDate couponExpiredAt;
    private String couponStoreType;
    private int couponMinAmount;
    private int couponMaxAmount;
}