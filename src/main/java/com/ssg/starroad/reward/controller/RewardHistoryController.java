package com.ssg.starroad.reward.controller;

import com.ssg.starroad.reward.service.RewardHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reward-history")
@RequiredArgsConstructor
public class RewardHistoryController {
    private final RewardHistoryService rewardHistoryService;
}
