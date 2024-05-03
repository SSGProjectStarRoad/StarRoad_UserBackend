package com.ssg.starroad.coupon.entity;

import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.review.enums.ConfidenceType;
import com.ssg.starroad.shop.entity.Store;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@ToString
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String complexShoppingmall;
    private String shopType;
    private String name;
    private int discountAmount;
    private int discountRate;
    private int minAmount;
    private int maxAmount;
    private String status;
    private LocalDate expiredAt;
}
