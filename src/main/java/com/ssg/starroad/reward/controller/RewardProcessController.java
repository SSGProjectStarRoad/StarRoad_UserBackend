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


    @GetMapping("/{email}/start")
    public ResponseEntity<?> startRewardProcess(@PathVariable String email) {
        // 여기서 userId와 DTO를 이용하여 처리 로직 호출
        rewardProcessService.startRewardProcess(email);
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

    @PatchMapping("/{email}/review")
    public ResponseEntity<?> updateReviewCount(@PathVariable String email) {
        try {
            rewardProcessService.updateReviewCount(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating review count: " + e.getMessage());
        }
    }

    @PatchMapping("/{email}/completed")
    public ResponseEntity<?> updateStatus(@PathVariable String email) {
        try {
            rewardProcessService.updateStatus(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
    @PatchMapping("/{email}/new")
    public ResponseEntity<?> resetStatus(@PathVariable String email) {
        try {
            rewardProcessService.resetStatus(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }

    @GetMapping("/{email}/get")
    public ResponseEntity<?> getProcess(@PathVariable("email") String email){
        RewardProcessDTO rewardProcessDTO=rewardProcessService.getProcess(email);
        if (rewardProcessDTO==null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rewardProcessDTO);  // 내용이 있을 경우 OK (200)와 함께 데이터 반환
    }


}
