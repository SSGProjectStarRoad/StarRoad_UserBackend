package com.ssg.starroad.reward.controller;

import com.ssg.starroad.reward.DTO.RewardDTO;
import com.ssg.starroad.reward.DTO.RewardMemberDTO;
import com.ssg.starroad.reward.service.RewardHistoryService;
import com.ssg.starroad.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;
    private final RewardHistoryService rewardHistoryService;

    @GetMapping("/list")
    public ResponseEntity<?> RewardList (){
        List<RewardDTO> rewardDTOS = rewardService.getRewardList();
        if (rewardDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rewardDTOS);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }

    @PostMapping("/obtain")
    public ResponseEntity<?> Rewardobtain(@RequestBody RewardMemberDTO rewardmemberDTO){
        rewardHistoryService.addReward(rewardmemberDTO);
        return ResponseEntity.ok("Reward issued successfully to user email " + rewardmemberDTO.getEmail());
    }
}
