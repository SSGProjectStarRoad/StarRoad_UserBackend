package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.Review;
import com.ssg.starroad.review.entity.ReviewLike;
import com.ssg.starroad.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewId);
    void deleteByUserIdAndReviewId(Long userId, Long reviewId);
    @Query("SELECT rl.review.id FROM ReviewLike rl WHERE rl.user.id = :userId")
    List<Long> findLikedReviewIdsByUserId(@Param("userId")Long userId);
    boolean existsByUserAndReview(User user, Review review);
    boolean existsByUser_IdAndReview_Id(Long userId, Long reviewId);
}
