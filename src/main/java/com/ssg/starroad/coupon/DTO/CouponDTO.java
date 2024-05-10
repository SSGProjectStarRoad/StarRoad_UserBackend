package com.ssg.starroad.coupon.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @JsonProperty("coupon_history_id")
    private Long couponHistoryId;

    @JsonProperty("coupon_id")
    private Long couponId;

    @JsonProperty("coupon_name")
    private String couponName;

    @JsonProperty("coupon_discount_rate")
    private int couponDiscountRate;

    @JsonProperty("coupon_discount_amount")
    private int couponDiscountAmount;

    @JsonProperty("coupon_status")
    private String couponStatus;

    @JsonProperty("coupon_usage_status")
    private boolean couponUsageStatus;

    @JsonProperty("coupon_expired_at")
    private LocalDate couponExpiredAt;

    @JsonProperty("coupon_store_type")
    private String couponShopType;

    @JsonProperty("coupon_min_amount")
    private int couponMinAmount;

    @JsonProperty("coupon_max_amount")
    private int couponMaxAmount;
}