package com.ssg.starroad.review.service;

public interface ReviewLikeService {
    void addLike(Long userId, Long reviewId);
    void removeLike(Long userId, Long reviewId);
    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
}