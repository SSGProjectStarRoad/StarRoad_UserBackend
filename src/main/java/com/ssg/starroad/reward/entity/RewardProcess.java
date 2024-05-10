package com.ssg.starroad.reward.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "REWARD_PROCESS")
public class RewardProcess {


    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;  // Primary key로 사용

    @Column(name = "REVIEW_COUNT", nullable = false)
    private int reviewCount = 0;

    @Column(name = "COUPON_COUNT", nullable = false)
    private int couponCount = 0;

    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDate expiredAt = LocalDate.now().plusDays(7);

    @Column(name = "REWARD_STATUS", nullable = false)
    private boolean rewardStatus = false;
    @Column(name = "USAGE_STATUS", nullable = false)
    private boolean usageStatus = false;
    @Column(name = "ISSUE_STATUS", nullable = false)
    private boolean issueStatus = false;

    public RewardProcess(Long userId) {
        this.userId = userId;
    }
}
