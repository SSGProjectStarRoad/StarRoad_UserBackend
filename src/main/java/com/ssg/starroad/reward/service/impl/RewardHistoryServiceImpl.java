package com.ssg.starroad.reward.service.impl;

import com.ssg.starroad.reward.repository.RewardHistoryRepository;
import com.ssg.starroad.reward.service.RewardHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardHistoryServiceImpl implements RewardHistoryService {
    private final RewardHistoryRepository rewardHistoryRepository;
}
