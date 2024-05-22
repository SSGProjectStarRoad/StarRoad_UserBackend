package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {

    Optional<List<ReviewFeedback>> findReviewFeedbackByReviewId(Long reviewId);
}
