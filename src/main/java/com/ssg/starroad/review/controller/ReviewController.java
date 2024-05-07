package com.ssg.starroad.review.controller;

import com.ssg.starroad.review.dto.ReviewRequest;
import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/write")
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest reviewRequest) {
        Review createdReview = reviewService.createReview(reviewRequest.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }
}
