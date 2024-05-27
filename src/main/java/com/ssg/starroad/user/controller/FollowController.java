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

    @GetMapping("/mycount/{email}")
    public ResponseEntity<FollowCountDTO> getCountByFromUserId(@PathVariable String email) {
        long followerCount = followService.getCountByFromUserId(email);
        long followingCount = followService.getCountByToUserId(email);
        FollowCountDTO followCountDTO = new FollowCountDTO(followerCount, followingCount);
        return ResponseEntity.ok(followCountDTO);
    }

    @GetMapping("/from/{email}")
    public ResponseEntity<List<FollowDTO>> getFollowsByFromUserId(@PathVariable String email) {
        List<FollowDTO> follows = followService.getFollowsByFromUserId(email);
        return ResponseEntity.ok(follows);
    }

    @GetMapping("/to/{email}")
    public ResponseEntity<List<FollowDTO>> getFollowsByToUserId(@PathVariable String email) {
        List<FollowDTO> follows = followService.getFollowsByToUserId(email);
        return ResponseEntity.ok(follows);
    }

    @DeleteMapping("/{id}/deletefrom/{email}")
    public ResponseEntity<Void> deleteFollowingUser(@PathVariable String email, @PathVariable Long id) {
        followService.deleteFollowingUser(email, id);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{id}/deleteto/{userId}")
//    public ResponseEntity<?> deleteFollowsByToUserId(@PathVariable Long userId) {
//        List<FollowDTO> follows = followService.getFollowsByToUserId(userId); //from을 지워야함
//        return ResponseEntity.ok(follows);
//    }

}
