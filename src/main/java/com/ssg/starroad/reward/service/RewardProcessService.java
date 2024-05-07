package com.ssg.starroad.reward.service;

import com.ssg.starroad.reward.DTO.RewardProcessDTO;

public interface RewardProcessService {
    public void startRewardProcess(Long userId, RewardProcessDTO rewardProcessDTO);

    public void updateCouponCount(Long userId);

    public void updateReviewCount(Long userId);

    public void updateStatus(Long userId);

    public RewardProcessDTO getProcess(Long userId);
}




