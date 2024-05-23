package com.ssg.starroad.coupon.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@ToString
public class CouponHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long couponId;
    private boolean usageStatus;
    private LocalDate expiredAt;
    private int discountRate;

    public void useCoupon(){
        this.usageStatus=true;
    }
}
