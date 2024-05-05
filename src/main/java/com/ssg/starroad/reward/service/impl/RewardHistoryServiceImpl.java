package com.ssg.starroad.reward.service.impl;

import com.ssg.starroad.reward.DTO.RewardMemberDTO;
import com.ssg.starroad.reward.entity.RewardHistory;
import com.ssg.starroad.reward.repository.RewardHistoryRepository;
import com.ssg.starroad.reward.service.RewardHistoryService;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewardHistoryServiceImpl implements RewardHistoryService {
    private final RewardHistoryRepository rewardHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public void addReward(RewardMemberDTO rewardmemberDTO) {
        Optional<User> user=userRepository.findById(rewardmemberDTO.getMemberId());

        user.ifPresent(value -> rewardHistoryRepository.save(RewardHistory.builder()
                .rewardId(rewardmemberDTO.getRewardId())
                .user(value).build()));

    }
}
