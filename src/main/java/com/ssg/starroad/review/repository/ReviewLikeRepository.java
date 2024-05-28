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
    // 주어진 userId와 reviewId로 ReviewLike 엔티티를 조회합니다.
// userId와 reviewId를 기준으로 ReviewLike를 찾습니다. 존재하지 않을 경우 Optional.empty()를 반환합니다.
    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewId);

    // 주어진 userId와 reviewId로 ReviewLike 엔티티를 삭제합니다.
// userId와 reviewId를 기준으로 ReviewLike를 삭제합니다.
    void deleteByUserIdAndReviewId(Long userId, Long reviewId);

    // 주어진 userId로 사용자가 좋아요를 누른 리뷰 ID 목록을 조회합니다.
// JPQL 쿼리를 사용하여 userId를 기준으로 사용자가 좋아요를 누른 리뷰 ID 목록을 조회하여 반환합니다.
    @Query("SELECT rl.review.id FROM ReviewLike rl WHERE rl.user.id = :userId")
    List<Long> findLikedReviewIdsByUserId(@Param("userId") Long userId);

    // 주어진 User와 Review로 ReviewLike 존재 여부를 확인합니다.
// User와 Review를 기준으로 ReviewLike의 존재 여부를 확인하여 반환합니다.
    boolean existsByUserAndReview(User user, Review review);

    // 주어진 userId와 reviewId로 ReviewLike 존재 여부를 확인합니다.
// userId와 reviewId를 기준으로 ReviewLike의 존재 여부를 확인하여 반환합니다.
    boolean existsByUser_IdAndReview_Id(Long userId, Long reviewId);

}
