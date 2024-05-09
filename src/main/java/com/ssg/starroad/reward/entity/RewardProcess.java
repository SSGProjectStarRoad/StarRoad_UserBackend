package com.ssg.starroad.reward.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.ssg.starroad.common.entity.BaseTimeEntity;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "REWARD_PROCESS")
public class RewardProcess {

//    @Id
//    @OneToOne
//    @JoinColumn(name = "ID", referencedColumnName = "ID")
//    private User user;

    @Id
    @Column(name = "USER_ID")
    private int userId;

    @Column(name = "REVIEW_COUNT", nullable = false)
    private int reviewCount = 0;

    @Column(name = "COUPON_COUNT", nullable = false)
    private int couponCount = 0;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRED_AT", nullable = false)
    private Date expiredAt;

    @Column(name = "REWARD_STATUS", nullable = false)
    private boolean rewardStatus = false;

}