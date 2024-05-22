package com.ssg.starroad.coupon.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsageDTO {
    @JsonProperty("coupon_usage_status")
    private boolean couponUsageStatus;
}