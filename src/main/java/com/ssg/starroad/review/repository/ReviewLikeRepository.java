package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewLike;
import com.ssg.starroad.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewId);
    void deleteByUserIdAndReviewId(Long userId, Long reviewId);

    boolean existsByUserAndReview(User user, Review review);
}
