package com.ssg.starroad.review.repository;

import com.ssg.starroad.review.entity.ReviewSentiment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSentimentRepository extends JpaRepository<ReviewSentiment, Long> {
}
