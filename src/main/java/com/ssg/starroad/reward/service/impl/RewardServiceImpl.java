package com.ssg.starroad.reward.service.impl;

import com.ssg.starroad.reward.repository.RewardRepository;
import com.ssg.starroad.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;


}
