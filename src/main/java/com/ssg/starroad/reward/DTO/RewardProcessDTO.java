package com.ssg.starroad.reward.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RewardProcessDTO {

    private int reviewCount;
    private int couponCount;
    private boolean rewardStatus;
    private LocalDate expiredAt;
    private boolean usageStatus;
    private boolean issueStatus;
}
