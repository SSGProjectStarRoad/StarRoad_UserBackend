package com.ssg.starroad.reward.service;

import com.ssg.starroad.reward.DTO.RewardHistoryDTO;
import com.ssg.starroad.reward.DTO.RewardMemberDTO;

import java.util.List;

public interface RewardHistoryService {
    public void addReward(RewardMemberDTO rewardmemberDTO);
    public List<RewardHistoryDTO> getRewardHistory(String email);
}
