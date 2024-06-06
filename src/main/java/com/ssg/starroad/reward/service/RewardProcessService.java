package com.ssg.starroad.reward.service;

import com.ssg.starroad.reward.DTO.RewardProcessDTO;

public interface RewardProcessService {
    public void startRewardProcess(String email);

    public void updateCouponCount(Long userId);

    public void updateReviewCount(String email);

    public void updateStatus(String email);
    public void resetStatus(String email);

    public RewardProcessDTO getProcess(String email);
}




