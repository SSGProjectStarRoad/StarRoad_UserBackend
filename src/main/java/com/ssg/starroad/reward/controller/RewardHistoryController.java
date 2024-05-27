package com.ssg.starroad.reward.controller;

import com.ssg.starroad.reward.DTO.RewardHistoryDTO;
import com.ssg.starroad.reward.entity.RewardHistory;
import com.ssg.starroad.reward.service.RewardHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reward-history")
@RequiredArgsConstructor
public class RewardHistoryController {
    private final RewardHistoryService rewardHistoryService;

    @GetMapping("/user/{email}/list")
    public ResponseEntity<List<RewardHistoryDTO>> getRewardHistory(@PathVariable("email") String email) {
        List<RewardHistoryDTO> rewardHistoryDTOList = rewardHistoryService.getRewardHistory(email);
        if (rewardHistoryDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();  // 내용이 없을 경우 No Content (204) 반환
        }
        return ResponseEntity.ok(rewardHistoryDTOList);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환

    }
}
