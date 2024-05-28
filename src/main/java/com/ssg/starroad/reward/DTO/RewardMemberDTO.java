package com.ssg.starroad.reward.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RewardMemberDTO {
    @JsonProperty("member_email")
    private String email;

    @JsonProperty("reward_id")
    private Long rewardId;
}
