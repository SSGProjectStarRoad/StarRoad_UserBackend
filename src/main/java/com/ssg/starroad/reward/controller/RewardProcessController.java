package com.ssg.starroad.reward.controller;


import com.ssg.starroad.reward.DTO.RewardProcessDTO;
import com.ssg.starroad.reward.service.RewardProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward-process")
@RequiredArgsConstructor
public class RewardProcessController {

    private final RewardProcessService rewardProcessService;


    @GetMapping("/{userId}/start")
    public ResponseEntity<?> startRewardProcess(@PathVariable Long userId) {
        // 여기서 userId와 DTO를 이용하여 처리 로직 호출
        rewardProcessService.startRewardProcess(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/coupon")
    public ResponseEntity<?> updateCouponCount(@PathVariable Long userId) {
        try {
            rewardProcessService.updateCouponCount(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating coupon count: " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}/review")
    public ResponseEntity<?> updateReviewCount(@PathVariable Long userId) {
        try {
            rewardProcessService.updateReviewCount(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating review count: " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}/completed")
    public ResponseEntity<?> updateStatus(@PathVariable Long userId) {
        try {
            rewardProcessService.updateStatus(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
    @PatchMapping("/{userId}/new")
    public ResponseEntity<?> resetStatus(@PathVariable Long userId) {
        try {
            rewardProcessService.resetStatus(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/get")
    public ResponseEntity<?> getProcess(@PathVariable("userId") Long userId){
        RewardProcessDTO rewardProcessDTO=rewardProcessService.getProcess(userId);
        if (rewardProcessDTO==null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rewardProcessDTO);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }


}
