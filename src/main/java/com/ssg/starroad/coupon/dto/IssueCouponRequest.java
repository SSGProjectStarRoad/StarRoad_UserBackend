package com.ssg.starroad.coupon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueCouponRequest {
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("couponId")
    private Long couponId;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
