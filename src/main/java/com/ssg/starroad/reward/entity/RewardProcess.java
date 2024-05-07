package com.ssg.starroad.reward.entity;

import com.ssg.starroad.reward.DTO.RewardProcessDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.ssg.starroad.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor
@Setter
@Table(name = "REWARD_PROCESS")
public class RewardProcess {

//    @Id
//    @OneToOne
//    @JoinColumn(name = "ID", referencedColumnName = "ID")
//    private User user;

    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "REVIEW_COUNT", nullable = false)
    private int reviewCount = 0;

    @Column(name = "COUPON_COUNT", nullable = false)
    private int couponCount = 0;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDate expiredAt;

    @Column(name = "REWARD_STATUS", nullable = false)
    private boolean rewardStatus = false;

    public RewardProcess(Long userId, RewardProcessDTO rewardProcessDTO) {
        this.userId=userId;
        this.couponCount=rewardProcessDTO.getCouponCount();
        this.reviewCount=rewardProcessDTO.getReviewCount();
        this.expiredAt=LocalDate.now().plusDays(7);
        this.rewardStatus=false;
    }
}