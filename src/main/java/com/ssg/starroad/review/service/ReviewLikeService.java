package com.ssg.starroad.review.service;

import com.ssg.starroad.review.DTO.ReviewLikeDTO;

import java.util.List;

public interface ReviewLikeService {
    ReviewLikeDTO addLike(String userEmail, Long reviewId);
    void removeLike(Long userId, Long reviewId);
    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
    List<Long> getLikedReviewIdsByUserId(Long userId);
}