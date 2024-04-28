package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFeedbackRepository extends JpaRepository<ReviewFeedback, Long> {
}
