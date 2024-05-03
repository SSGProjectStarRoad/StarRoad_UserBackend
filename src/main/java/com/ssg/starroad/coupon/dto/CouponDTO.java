package com.ssg.starroad.coupon.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
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
    private String couponShopType;
    private int couponMinAmount;
    private int couponMaxAmount;
}