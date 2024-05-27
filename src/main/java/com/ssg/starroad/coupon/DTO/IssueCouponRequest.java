package com.ssg.starroad.coupon.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueCouponRequest {
    @JsonProperty("email")
    private String email;
    @JsonProperty("couponId")
    private Long couponId;

    // Getters and Setters
    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String userId) {
        this.email = userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
