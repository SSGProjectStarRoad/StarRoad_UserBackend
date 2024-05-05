package com.ssg.starroad.reward.service.impl;

import com.ssg.starroad.reward.DTO.RewardDTO;
import com.ssg.starroad.reward.DTO.RewardMemberDTO;
import com.ssg.starroad.reward.repository.RewardRepository;
import com.ssg.starroad.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;
    private final ModelMapper modelMapper;

    public List<RewardDTO> getRewardList(){
        List<RewardDTO> rewardDTOs = rewardRepository.findAll().stream()
                .map(reward -> modelMapper.map(reward,RewardDTO.class)).toList();

        return rewardDTOs;
    }


}
