package com.ssg.starroad.reward.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardHistoryDTO {

    @JsonProperty("reward_id")
    private Long rewardId;

    @JsonProperty("reward_name")
    private String rewardName;

    @JsonProperty("reward_count")
    private int count;

}