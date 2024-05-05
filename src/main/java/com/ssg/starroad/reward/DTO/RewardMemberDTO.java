package com.ssg.starroad.reward.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RewardMemberDTO {
    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("reward_id")
    private int rewardId;
}
