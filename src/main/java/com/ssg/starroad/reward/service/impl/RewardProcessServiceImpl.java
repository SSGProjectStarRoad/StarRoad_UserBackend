package com.ssg.starroad.reward.service.impl;

import com.ssg.starroad.reward.DTO.RewardProcessDTO;
import com.ssg.starroad.reward.entity.RewardProcess;
import com.ssg.starroad.reward.repository.RewardProcessRepository;
import com.ssg.starroad.reward.service.RewardProcessService;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewardProcessServiceImpl implements RewardProcessService {

    private final RewardProcessRepository rewardProcessRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void startRewardProcess(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow();  // 존재하지 않는 경우 새 객체 생성
        rewardProcess.setCouponCount(rewardProcess.getCouponCount()+1);
        rewardProcessRepository.save(rewardProcess);  // 저장
    }

    @Override
    @Transactional
    public void updateCouponCount(Long userId) {
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        rewardProcess.setCouponCount(rewardProcess.getCouponCount() + 1);
        rewardProcessRepository.save(rewardProcess);
    }

    @Override
    @Transactional
    public void updateReviewCount(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        rewardProcess.setReviewCount(rewardProcess.getReviewCount() + 1);
        rewardProcessRepository.save(rewardProcess);
    }

    @Override
    @Transactional
    public void updateStatus(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        rewardProcess.setRewardStatus(true);
        rewardProcessRepository.save(rewardProcess);
    }

    @Override
    public void resetStatus(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        rewardProcess.setIssueStatus(false);
        rewardProcess.setUsageStatus(false);
        rewardProcessRepository.save(rewardProcess);
    }

    @Override
    @Transactional
    public RewardProcessDTO getProcess(String email){
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId).orElseGet(() -> {
            // RewardProcess가 존재하지 않을 경우 새로운 보상 프로세스를 생성합니다.
            RewardProcess newRewardProcess = new RewardProcess();
            newRewardProcess.setUserId(userId);
            newRewardProcess.setCouponCount(0);
            newRewardProcess.setReviewCount(0);
            newRewardProcess.setIssueStatus(false);
            newRewardProcess.setUsageStatus(false);
            newRewardProcess.setRewardStatus(false);
            newRewardProcess.setExpiredAt(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))); // 예시로 만료일을 7일 후로 설정
            return rewardProcessRepository.save(newRewardProcess);
        });

        if(Objects.equals(rewardProcess.getExpiredAt(), LocalDate.now()))
        {
           rewardProcessRepository.delete(rewardProcess);
        }
        return modelMapper.map(rewardProcess,RewardProcessDTO.class);
    }

}
