package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.DTO.ReviewLikeDTO;
import com.ssg.starroad.review.service.ReviewLikeService;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review-likes")
@RequiredArgsConstructor
public class ReviewLikeController {
    private final ReviewLikeService reviewLikeService;
    private final UserRepository userRepository;

    @GetMapping("/{userEmail}")
    public ResponseEntity<List<Long>> getUserLikes(@PathVariable String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<Long> likedReviewIds = reviewLikeService.getLikedReviewIdsByUserId(user.getId());
        return ResponseEntity.ok(likedReviewIds);
    }

    @PostMapping("/{reviewId}/{userEmail}")
    public ResponseEntity<ReviewLikeDTO> addLike(@PathVariable Long reviewId, @PathVariable String userEmail) {

        ReviewLikeDTO response = reviewLikeService.addLike(userEmail, reviewId);
        return ResponseEntity.ok(response);
    }
}
