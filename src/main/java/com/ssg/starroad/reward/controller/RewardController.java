package com.ssg.starroad.reward.controller;

import com.ssg.starroad.reward.DTO.RewardDTO;
import com.ssg.starroad.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @GetMapping("/list")
    public ResponseEntity<?> RewardList (){
        List<RewardDTO> rewardDTOS = rewardService.getRewardList();
        if (rewardDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(rewardDTOS);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }
}
