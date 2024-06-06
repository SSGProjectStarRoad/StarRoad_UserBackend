package com.ssg.starroad.reward.service.impl;

import com.querydsl.core.Tuple;
import com.ssg.starroad.reward.DTO.RewardHistoryDTO;
import com.ssg.starroad.reward.DTO.RewardMemberDTO;
import com.ssg.starroad.reward.entity.RewardHistory;
import com.ssg.starroad.reward.repository.RewardHistoryRepository;
import com.ssg.starroad.reward.repository.impl.RewardHistoryRepositoryCustom;
import com.ssg.starroad.reward.service.RewardHistoryService;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewardHistoryServiceImpl implements RewardHistoryService {
    private final RewardHistoryRepository rewardHistoryRepository;
    private final RewardHistoryRepositoryCustom rewardHistoryRepositoryCustom;
    private final UserRepository userRepository;

    @Override
    public void addReward(RewardMemberDTO rewardmemberDTO) {
        Optional<User> user=userRepository.findByEmail(rewardmemberDTO.getEmail());

        user.ifPresent(value -> rewardHistoryRepository.save(RewardHistory.builder()
                .rewardId(rewardmemberDTO.getRewardId())
                .user(value).build()));
    }

    @Override
    public List<RewardHistoryDTO> getRewardHistory(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        List<Tuple> rewardCounts = rewardHistoryRepositoryCustom.countRewardHistoryByUserId(userId);
        List<RewardHistoryDTO> result = new ArrayList<>();

        for (Tuple record : rewardCounts) {
            RewardHistoryDTO dto = RewardHistoryDTO.builder()
                    .rewardId(record.get(0, Long.class))
                    .rewardName(record.get(1, String.class))
                    .count(Objects.requireNonNull(record.get(2, Long.class)))
                    .rewardImagePath(record.get(3,String.class))
                    .build();
            result.add(dto);
        }
        return result;
    }
}
