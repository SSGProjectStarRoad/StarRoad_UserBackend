package com.ssg.starroad.reward.service.impl;

import com.ssg.starroad.reward.DTO.RewardProcessDTO;
import com.ssg.starroad.reward.entity.RewardProcess;
import com.ssg.starroad.reward.repository.RewardProcessRepository;
import com.ssg.starroad.reward.service.RewardProcessService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewardProcessServiceImpl implements RewardProcessService {

    private final RewardProcessRepository rewardProcessRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void startRewardProcess(Long userId, RewardProcessDTO rewardProcessDTO) {
        Optional<RewardProcess> optionalrewardProcess=rewardProcessRepository.findById(userId);
        if(optionalrewardProcess.isEmpty()){
            rewardProcessRepository.save(new RewardProcess(userId, rewardProcessDTO));
        }
        else {
            RewardProcess rewardProcess = optionalrewardProcess.get();
            rewardProcess.setRewardStatus(false);
            rewardProcess.setExpiredAt(LocalDate.now().plusDays(7));
            rewardProcess.setReviewCount(0);
            rewardProcess.setCouponCount(0);
            rewardProcessRepository.save(rewardProcess);
        }
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
    public void updateReviewCount(Long userId) {
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        rewardProcess.setCouponCount(rewardProcess.getReviewCount() + 1);
        rewardProcessRepository.save(rewardProcess);
    }

    @Override
    @Transactional
    public void updateStatus(Long userId) {
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        rewardProcess.setRewardStatus(true);
        rewardProcessRepository.save(rewardProcess);
    }

    @Override
    @Transactional
    public RewardProcessDTO getProcess(Long userId){
        RewardProcess rewardProcess = rewardProcessRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        return modelMapper.map(rewardProcess,RewardProcessDTO.class);
    }

}
