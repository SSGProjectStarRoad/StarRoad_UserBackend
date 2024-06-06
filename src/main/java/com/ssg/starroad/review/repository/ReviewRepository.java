package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 매장의 리뷰를 페이징하여 조회하는 메서드
    // `id`는 매장 ID를 의미하며, `pageable`은 페이징 정보를 포함합니다.
    Page<Review> findAllWithPageByStoreId(Long id, Pageable pageable);

    // 특정 매장의 모든 리뷰를 조회하는 메서드
    // `id`는 매장 ID를 의미합니다.
    Optional<List<Review>> findAllByStoreId(Long id);

    // 특정 유저의 리뷰 개수를 조회하는 메서드
    // `userId`는 유저 ID를 의미합니다.
    Long countByUserId(Long userId);

    // 특정 매장의 리뷰 개수를 조회하는 메서드
    // `storeId`는 매장 ID를 의미합니다.
    Long countByStoreId(Long storeId);

    // 특정 매장에 대해 특정 피드백 선택지를 가진 리뷰 피드백 개수를 조회하는 메서드
    // `storeId`는 매장 ID를 의미하고, `selection`은 피드백 선택지를 의미합니다.
    @Query("SELECT COUNT(rf) FROM ReviewFeedback rf WHERE rf.review.store.id = :storeId AND rf.reviewFeedbackSelection = :selection")
    Long countByStoreIdAndReviewFeedbackSelection(@Param("storeId") Long storeId, @Param("selection") String selection);

    // 모든 리뷰를 페이징하여 조회하는 메소드
    Page<Review> findAll(Pageable pageable);
    Page<Review> findAllWithPageByUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.user.id IN :userIds")
    Page<Review> findAllByUserIds(@Param("userIds") List<Long> userIds, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN ReviewFeedback rf ON r.id = rf.review.id WHERE r.store.id = :storeId AND rf.reviewFeedbackSelection = :selection")
    Page<Review> findAllWithPageByStoreIdAndReviewFeedbackSelection(@Param("storeId") Long storeId, @Param("selection") String selection, Pageable pageable);
    // 리뷰 ID 목록을 기반으로 페이징하여 리뷰를 조회하는 메서드 추가
    Page<Review> findByIdIn(List<Long> ids, Pageable pageable);

    // 새 메소드 추가
    Page<Review> findByStoreIdAndContentsContaining(Long storeId, String keyword, Pageable pageable);


}
