package com.ssg.starroad.review.service.impl;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewLike;
import com.ssg.starroad.review.repository.ReviewLikeRepository;
import com.ssg.starroad.review.repository.ReviewRepository;
import com.ssg.starroad.review.service.ReviewLikeService;
import com.ssg.starroad.user.entity.User;
import com.ssg.starroad.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void addLike(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        Optional<ReviewLike> reviewLikeOptional = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId);
        if (reviewLikeOptional.isPresent()) {
            // 이미 좋아요가 존재하면 좋아요 취소 (삭제) 로직
            reviewLikeRepository.delete(reviewLikeOptional.get());
        } else {
            // 좋아요가 존재하지 않으면 새로운 좋아요 추가
            ReviewLike reviewLike = ReviewLike.builder()
                    .user(user)
                    .review(review)
                    .build();
            reviewLikeRepository.save(reviewLike);
        }
    }

    @Override
    @Transactional
    public void removeLike(Long userId, Long reviewId) {
        reviewLikeRepository.deleteByUserIdAndReviewId(userId, reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndReviewId(Long userId, Long reviewId) {
        return reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId).isPresent();
    }
}
