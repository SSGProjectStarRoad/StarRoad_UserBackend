package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review-likes")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/{reviewId}/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewLikeService.addLike(userId, reviewId);
        return ResponseEntity.ok().build();
    }
}
