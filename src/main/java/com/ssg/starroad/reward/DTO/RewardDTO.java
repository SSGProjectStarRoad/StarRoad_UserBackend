package com.ssg.starroad.reward.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {
    @JsonProperty("reward_id")
    private int rewardId;

    @JsonProperty("reward_name")
    private String rewardName;

    @JsonProperty("has_reward")
    private boolean hasReward;
}
