package com.ssg.starroad.user.controller;

import com.ssg.starroad.user.dto.FollowCountDTO;
import com.ssg.starroad.user.dto.FollowDTO;
import com.ssg.starroad.user.service.FollowService;
import com.ssg.starroad.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/follow")
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("/mycount/{userId}")
    public ResponseEntity<FollowCountDTO> getCountByFromUserId(@PathVariable Long userId) {
        long followerCount = followService.getCountByFromUserId(userId);
        long followingCount = followService.getCountByToUserId(userId);
        FollowCountDTO followCountDTO = new FollowCountDTO(followerCount, followingCount);
        return ResponseEntity.ok(followCountDTO);
    }

    @GetMapping("/from/{userId}")
    public ResponseEntity<List<FollowDTO>> getFollowsByFromUserId(@PathVariable Long userId) {
        List<FollowDTO> follows = followService.getFollowsByFromUserId(userId);
        return ResponseEntity.ok(follows);
    }

    @GetMapping("/to/{userId}")
    public ResponseEntity<List<FollowDTO>> getFollowsByToUserId(@PathVariable Long userId) {
        List<FollowDTO> follows = followService.getFollowsByToUserId(userId);
        return ResponseEntity.ok(follows);
    }

    @DeleteMapping("/{id}/deletefrom/{userId}")
    public ResponseEntity<Void> deleteFollowingUser(@PathVariable Long userId, @PathVariable Long id) {
        followService.deleteFollowingUser(userId, id);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{id}/deleteto/{userId}")
//    public ResponseEntity<?> deleteFollowsByToUserId(@PathVariable Long userId) {
//        List<FollowDTO> follows = followService.getFollowsByToUserId(userId); //from을 지워야함
//        return ResponseEntity.ok(follows);
//    }

}
